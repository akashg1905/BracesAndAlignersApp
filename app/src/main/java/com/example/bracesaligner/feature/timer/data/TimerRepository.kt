package com.example.bracesaligner.feature.timer.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.core.database.dao.AlignerPlanDao
import com.example.bracesaligner.core.database.dao.NonWearTimerDao
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity
import com.example.bracesaligner.core.network.api.NotificationApi
import com.example.bracesaligner.core.network.api.TimerApi
import retrofit2.HttpException
import com.example.bracesaligner.core.network.dto.DailySummaryRequest
import com.example.bracesaligner.core.network.dto.NotificationDispatchRequest
import com.example.bracesaligner.core.network.dto.TimerSessionRequest
import com.example.bracesaligner.core.network.dto.TimerSessionResponse
import com.example.bracesaligner.feature.timer.domain.TimerThresholdEvaluator
import com.example.bracesaligner.core.preferences.SessionStore
import com.example.bracesaligner.feature.notifications.TimerAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerRepository @Inject constructor(
    private val timerDao: NonWearTimerDao,
    private val planDao: AlignerPlanDao,
    private val timerApi: TimerApi,
    private val notificationApi: NotificationApi,
    private val sessionStore: SessionStore,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "TimerRepository"
    }

    private val warningMinutes = 90
    private val limitMinutes = 120
    private val thresholdEvaluator = TimerThresholdEvaluator()
    private val dispatchMutex = Mutex()

    fun observeTimerState(): Flow<TimerState> {
        val today = TimeUtils.todayEpochDay()
        return combine(
            timerDao.observeActiveSession(),
            timerDao.observeDayTotalMillis(today)
        ) { active, totalMillis ->
            TimerState(
                isRunning = active != null,
                activeSessionStart = active?.startEpochMillis,
                todayTotalMillis = totalMillis,
                warningMinutes = warningMinutes,
                limitMinutes = limitMinutes
            )
        }
    }

    private suspend fun getCurrentAlignerNumber(): Int {
        val plan = planDao.getPlan() ?: return 1
        val today = TimeUtils.todayEpochDay()
        val daysSinceStart = (today - plan.startDateEpochDay).toInt()
        if (daysSinceStart < 0) return 1
        val currentAligner = (daysSinceStart / plan.daysPerAligner) + 1
        return currentAligner.coerceAtMost(plan.alignerCount)
    }

    suspend fun startTimer() {
        val existing = timerDao.getActiveSession()
        if (existing != null) {
            Log.w(TAG, "[START] Ignored, active session already exists: ${existing.sessionId}")
            return
        }
        val now = TimeUtils.nowMillis()
        val alignerNumber = getCurrentAlignerNumber()
        val sessionId = UUID.randomUUID().toString()
        val session = NonWearSessionEntity(
            sessionId = sessionId,
            alignerNumber = alignerNumber,
            startEpochMillis = now,
            endEpochMillis = null,
            dateEpochDay = TimeUtils.epochDayFromMillis(now)
        )
        timerDao.upsertSession(session)

        Log.i(TAG, "[START] Session persisted successfully locally")
        scheduleNextAlarm(context)
    }

    suspend fun stopTimer() {
        val active = timerDao.getActiveSession()
        if (active == null) {
            Log.w(TAG, "[STOP] Ignored, no active session found")
            return
        }
        val now = TimeUtils.nowMillis()
        Log.i(TAG, "[STOP] Stopping session=${active.sessionId}, started=${active.startEpochMillis}, ended=$now")
        timerDao.stopSession(active.sessionId, now)
        cancelAlarm(context)
        
        // Immediate sync of the session just finished
        try {
            Log.d(TAG, "[STOP] Syncing finished session to backend")
            val planId = planDao.getPlan()?.planId
            timerApi.syncSession(
                TimerSessionRequest(
                    sessionId = active.sessionId,
                    planId = planId,
                    alignerNumber = active.alignerNumber,
                    startEpochMillis = active.startEpochMillis,
                    endEpochMillis = now
                )
            )
            timerDao.markAsSynced(listOf(active.sessionId))
            Log.i(TAG, "[STOP] Session sync success, marked synced")
        } catch (e: Exception) {
            Log.e(TAG, "[STOP] Session sync failed; will retry in background", e)
            // If it fails, it remains unsynced in DB and will be picked up by background sync
        }

        val epochDay = TimeUtils.todayEpochDay()
        val totalMillis = timerDao.getDayTotalMillis(epochDay)
        val totalMinutes = (totalMillis / 60000).toInt()
        val threshold = thresholdEvaluator.evaluate(totalMinutes, warningMinutes, limitMinutes)
        timerDao.upsertDailySummary(
            DailyNonWearSummaryEntity(
                dateEpochDay = epochDay,
                totalMinutes = totalMinutes,
                warningSent = threshold.warningReached,
                exceededSent = threshold.limitExceeded
            )
        )
        Log.d(TAG, "[STOP] Daily summary updated: day=$epochDay totalMinutes=$totalMinutes warning=${threshold.warningReached} exceeded=${threshold.limitExceeded}")

        refreshSummary()
    }

    suspend fun refreshSummary() {
        try {
            val planId = planDao.getPlan()?.planId
            Log.d(TAG, "[SUMMARY] Fetching summary for state restoration, planId=$planId")
            val response = timerApi.getSummary(planId = planId, includeDaily = true)
            
            // 1. Update Average Wear Hours in SessionStore
            response.averageDailyWearHours?.let {
                sessionStore.saveAverageWearHours(it, response.averageDailyWearDisplay)
            }

            // 2. Restore Daily Breakdowns and individual sessions
            var activeSessionFromBreakdown: com.example.bracesaligner.core.network.dto.TimerSessionResponse? = null
            
            response.dailyBreakdown?.forEach { breakdown ->
                val date = try {
                    java.time.LocalDate.parse(breakdown.calendarDate)
                } catch (e: Exception) {
                    Log.e(TAG, "[RESTORE] Failed to parse date: ${breakdown.calendarDate}")
                    null
                } ?: return@forEach

                val breakdownEpochDay = date.toEpochDay()
                
                // Update the daily summary table (for historical progress/charts)
                val existing = timerDao.getDailySummary(breakdownEpochDay)
                val totalMinutesCalculated = (breakdown.nonWearHours * 60).toInt()
                
                timerDao.upsertDailySummary(
                    DailyNonWearSummaryEntity(
                        dateEpochDay = breakdownEpochDay,
                        totalMinutes = totalMinutesCalculated,
                        warningSent = existing?.warningSent ?: false,
                        exceededSent = existing?.exceededSent ?: false
                    )
                )

                // 3. Restore individual sessions
                breakdown.sessions?.forEach { session ->
                    // Use the device's local day calculation for consistency with the "Today" query
                    val sessionDay = TimeUtils.epochDayFromMillis(session.startEpochMillis)
                    
                    timerDao.upsertSession(
                        NonWearSessionEntity(
                            sessionId = session.sessionId,
                            alignerNumber = session.alignerNumber,
                            startEpochMillis = session.startEpochMillis,
                            endEpochMillis = session.endEpochMillis,
                            dateEpochDay = sessionDay,
                            synced = true
                        )
                    )

                    // Identify if this is the active session
                    if (session.endEpochMillis == null) {
                        activeSessionFromBreakdown = session
                    }
                }
            }

            // 4. Handle active session restoration (priority: top-level, fallback: breakdown)
            val activeToRestore = response.activeSession ?: activeSessionFromBreakdown
            if (activeToRestore != null) {
                timerDao.upsertSession(
                    NonWearSessionEntity(
                        sessionId = activeToRestore.sessionId,
                        alignerNumber = activeToRestore.alignerNumber,
                        startEpochMillis = activeToRestore.startEpochMillis,
                        endEpochMillis = activeToRestore.endEpochMillis,
                        dateEpochDay = TimeUtils.epochDayFromMillis(activeToRestore.startEpochMillis),
                        synced = true
                    )
                )
                Log.i(TAG, "[SUMMARY] Restored active session: ${activeToRestore.sessionId}")
                scheduleNextAlarm(context)
            } else {
                // Final check: did we restore an active session from the DB?
                if (timerDao.getActiveSession() != null) {
                    Log.i(TAG, "[SUMMARY] Resuming active session from local DB")
                    scheduleNextAlarm(context)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[SUMMARY] Failed to refresh summary", e)
        }
    }

    suspend fun syncPendingSessions() {
        try {
            val unsynced = timerDao.getUnsyncedSessions()
            if (unsynced.isEmpty()) {
                Log.d(TAG, "[SYNC_PENDING] No unsynced sessions")
                return
            }
            Log.i(TAG, "[SYNC_PENDING] Found ${unsynced.size} unsynced session(s)")

            val planId = planDao.getPlan()?.planId
            unsynced.forEach { session ->
                Log.d(TAG, "[SYNC_PENDING] Syncing session=${session.sessionId}")
                timerApi.syncSession(
                    TimerSessionRequest(
                        sessionId = session.sessionId,
                        planId = planId,
                        alignerNumber = session.alignerNumber,
                        startEpochMillis = session.startEpochMillis,
                        endEpochMillis = session.endEpochMillis!!
                    )
                )
            }
            
            timerDao.markAsSynced(unsynced.map { it.sessionId })
            Log.i(TAG, "[SYNC_PENDING] All pending sessions marked synced")
        } catch (e: Exception) {
            Log.e(TAG, "[SYNC_PENDING] Failed to sync pending sessions", e)
        }
    }

    fun observeWeeklySummary() = timerDao.observeRecentSummary(7)

    suspend fun checkAndDispatchNonWearNotifications(source: String = "unknown") {
        Log.i(TAG, "[DISPATCH_CHECK][$source] Started")
        val activeSession = timerDao.getActiveSession()
        
        if (activeSession == null) {
            val allSessions = timerDao.getAllSessions()
            Log.w(TAG, "[DISPATCH_CHECK][$source] No active session. sessionsInDb=${allSessions.size}")
            if (allSessions.isNotEmpty()) {
                val last = allSessions.first()
                Log.d(TAG, "[DISPATCH_CHECK][$source] Last session id=${last.sessionId} start=${last.startEpochMillis} end=${last.endEpochMillis}")
            }
            return
        }

        val now = TimeUtils.nowMillis()
        val elapsedMillis = now - activeSession.startEpochMillis
        val elapsedMinutes = (elapsedMillis / 60000).toInt()
        Log.i(TAG, "[DISPATCH_CHECK][$source] session=${activeSession.sessionId} elapsedMin=$elapsedMinutes lastNotifiedMin=${activeSession.lastNotificationMinutes}")

        // Milestones every 5 minutes
        val intervals = (1..288).map { it * 5 }
        
        // Find the highest milestone we have crossed but haven't notified for yet
        val nextInterval = intervals.lastOrNull { 
            elapsedMinutes >= it && it > activeSession.lastNotificationMinutes 
        }

        if (nextInterval == null) {
            Log.d(TAG, "[DISPATCH_CHECK][$source] No milestone crossed yet. nextTargetMin=${((activeSession.lastNotificationMinutes / 5) + 1) * 5}")
            return
        }

        try {
            dispatchMutex.withLock {
                val today = TimeUtils.todayEpochDay()
                val finishedMillis = timerDao.getDayTotalMillis(today)
                val totalNonWearMinutes = ((finishedMillis + elapsedMillis) / 60000).toInt()

                Log.i(TAG, "[DISPATCH_CHECK][$source] Dispatching /api/notifications/dispatch for milestone=$nextInterval. totalNonWearMinutes=$totalNonWearMinutes")
                val code = "NF100"
                
                notificationApi.dispatchNotification(
                    listOf(
                        NotificationDispatchRequest(
                            code = code,
                            nonWearTime = totalNonWearMinutes
                        )
                    )
                )
                
                // Update DB ONLY after successful API call
                timerDao.updateLastNotification(activeSession.sessionId, nextInterval)
                Log.i(TAG, "[DISPATCH_CHECK][$source] Dispatch success for milestone=$nextInterval. DB updated.")
            }
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch 422/Error: $errorBody")
            }
            Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch FAILED for milestone=$nextInterval. Will retry on next check.", e)
        }
    }

    fun scheduleNextAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // We need to fetch the active session to know when the next 5-min milestone is
        // Since this might be called from a non-suspend context or we want it simple:
        // Ideally, this logic should be inside a coroutine or we use a simplified version.
        // For simplicity in the repository, let's assume we want to check every 5 mins from start.
        
        // Better: Fetch active session from DB (blocking if needed or launch in scope)
        // However, AlarmManager works best if we schedule the *next* milestone precisely.
        
        // For now, let's schedule a 5-minute recurring-like one-shot for precision.
        val intent = Intent(context, TimerAlarmReceiver::class.java).apply {
            action = TimerAlarmReceiver.ACTION_CHECK_TIMER
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate next milestone (every 5 mins)
        val now = System.currentTimeMillis()
        // Here we ideally find (now - start) % 5min and schedule the remainder.
        // We schedule the next check exactly 5 minutes from now.
        val triggerAt = now + (5 * 60 * 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
                )
                Log.d(TAG, "[ALARM] Scheduled exact alarm at $triggerAt")
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
                )
                Log.d(TAG, "[ALARM] Scheduled inexact alarm (no permission) at $triggerAt")
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
            Log.d(TAG, "[ALARM] Scheduled exact alarm at $triggerAt")
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerAlarmReceiver::class.java).apply {
            action = TimerAlarmReceiver.ACTION_CHECK_TIMER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "[ALARM] Cancelled")
    }
}
