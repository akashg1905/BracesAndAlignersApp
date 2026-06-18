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
import com.example.bracesaligner.core.network.dto.NotificationDispatchRequest
import com.example.bracesaligner.core.network.dto.NotificationDispatchResponse
import com.example.bracesaligner.core.network.dto.TimerSessionRequest
import com.example.bracesaligner.core.network.dto.TimerSessionResponse
import com.example.bracesaligner.feature.timer.domain.TimerThresholdEvaluator
import com.example.bracesaligner.core.preferences.SessionStore
import com.example.bracesaligner.feature.notifications.NotificationHelper
import com.example.bracesaligner.feature.notifications.TimerForegroundService
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
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "TimerRepository"
        private const val REFRESH_THRESHOLD = 5 * 60 * 1000 // 5 minutes
    }

    private val warningMinutes = 90
    private val limitMinutes = 120
    private val thresholdEvaluator = TimerThresholdEvaluator()
    private val dispatchMutex = Mutex()
    private var lastRefreshTime = 0L

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

    fun observeSessionsForDay(epochDay: Long): Flow<List<NonWearSessionEntity>> {
        return timerDao.observeSessionsForDay(epochDay)
    }

    private suspend fun getCurrentAlignerNumber(): Int {
        val plan = planDao.getPlan() ?: return 1
        val today = TimeUtils.todayEpochDay()
        val daysSinceStart = (today - plan.startDateEpochDay).toInt()
        if (daysSinceStart < 0) return 1
        
        // Handle potential divide by zero if plan data is invalid
        if (plan.daysPerAligner <= 0) {
            Log.e(TAG, "[PLAN] Invalid daysPerAligner (${plan.daysPerAligner}) for plan ${plan.planId}. Defaulting to aligner 1.")
            return 1
        }

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
        
        val intent = Intent(context, TimerForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
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
        
        context.stopService(Intent(context, TimerForegroundService::class.java))
        
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

    suspend fun refreshSummary(force: Boolean = false) {
        if (!force && TimeUtils.nowMillis() - lastRefreshTime < REFRESH_THRESHOLD) {
            Log.d(TAG, "[SUMMARY] Skipping refresh, last sync was recent")
            return
        }
        try {
            val planId = planDao.getPlan()?.planId
            Log.d(TAG, "[SUMMARY] Fetching summary for state restoration, planId=$planId")
            val response = timerApi.getSummary(planId = planId, includeDaily = true)
            
            lastRefreshTime = TimeUtils.nowMillis()
            
            // 1. Update Average Wear Hours in SessionStore
            response.averageDailyWearHours?.let {
                sessionStore.saveAverageWearHours(it, response.averageDailyWearDisplay)
            }

            // 2. Restore Daily Breakdowns and individual sessions
            var activeSessionFromBreakdown: TimerSessionResponse? = null
            
            response.dailyBreakdown?.forEach { breakdown ->
                val breakdownEpochDay = try {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                    val date = sdf.parse(breakdown.calendarDate)
                    if (date != null) TimeUtils.epochDayFromMillis(date.time) else null
                } catch (_: Exception) {
                    Log.e(TAG, "[RESTORE] Failed to parse date: ${breakdown.calendarDate}")
                    null
                } ?: return@forEach
                
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
                startForegroundServiceIfNeeded()
            } else {
                // Final check: did we restore an active session from the DB?
                if (timerDao.getActiveSession() != null) {
                    Log.i(TAG, "[SUMMARY] Resuming active session from local DB")
                    startForegroundServiceIfNeeded()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[SUMMARY] Failed to refresh summary", e)
        }
    }

    private fun startForegroundServiceIfNeeded() {
        val intent = Intent(context, TimerForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
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
            Log.w(TAG, "[DISPATCH_CHECK][$source] No active session.")
            return
        }

        val now = TimeUtils.nowMillis()
        val elapsedMillis = now - activeSession.startEpochMillis
        val elapsedMinutes = (elapsedMillis / 60000).toInt()
        
        val currentInterval = elapsedMinutes / 5
        val lastInterval = activeSession.lastNotificationMinutes / 5

        if (currentInterval <= lastInterval) {
            Log.d(TAG, "[DISPATCH_CHECK][$source] No new 5-minute milestone crossed. currentInterval=$currentInterval, lastInterval=$lastInterval")
            return
        }

        Log.i(TAG, "[DISPATCH_CHECK][$source] session=${activeSession.sessionId} elapsedMin=$elapsedMinutes lastNotifiedMin=${activeSession.lastNotificationMinutes}. New milestones to process: ${lastInterval + 1} to $currentInterval")

        for (i in lastInterval + 1..currentInterval) {
            val milestone = i * 5
            try {
                dispatchMutex.withLock {
                    // Re-fetch to ensure we have the most up-to-date 'lastNotificationMinutes'
                    val latestSession = timerDao.getActiveSession() ?: return@withLock
                    if (latestSession.lastNotificationMinutes >= milestone) {
                        Log.d(TAG, "[DISPATCH_CHECK][$source] Already notified for $milestone. Skipping.")
                        return@withLock
                    }

                    val today = TimeUtils.todayEpochDay()
                    val finishedMillis = timerDao.getDayTotalMillis(today)
                    // Use actual elapsed time for the non-wear time reporting
                    val totalNonWearMinutes = ((finishedMillis + (milestone * 60000L)) / 60000).toInt()

                    Log.i(TAG, "[DISPATCH_CHECK][$source] 🔔 TRIGGERING NOTIFICATION: Milestone ${milestone}m reached. Sending NF100.")
                    
                    val response = notificationApi.dispatchNotification(
                        listOf(
                            NotificationDispatchRequest(
                                code = "NF100",
                                nonWearTime = totalNonWearMinutes
                            )
                        )
                    )

                    Log.i(TAG, "[DISPATCH_CHECK][$source] 📥 Backend Response Received: $response. Relying on FCM for display.")
                    
                    // Update DB ONLY after successful API call
                    timerDao.updateLastNotification(activeSession.sessionId, milestone)
                    Log.i(TAG, "[DISPATCH_CHECK][$source] ✅ NOTIFICATION DISPATCHED SUCCESSFULLY: Milestone ${milestone}m recorded in DB.")
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch 422/Error: $errorBody")
                }
                Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch FAILED for milestone=$milestone. Will retry.", e)
                // Stop processing further milestones if one fails to maintain order/reliability
                break
            }
        }
    }

    // AlarmManager helper methods removed as per requirements
}
