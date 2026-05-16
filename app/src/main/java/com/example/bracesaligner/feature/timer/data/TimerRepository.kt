package com.example.bracesaligner.feature.timer.data

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
import com.example.bracesaligner.feature.timer.domain.TimerThresholdEvaluator
import com.example.bracesaligner.core.preferences.SessionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerRepository @Inject constructor(
    private val timerDao: NonWearTimerDao,
    private val planDao: AlignerPlanDao,
    private val timerApi: TimerApi,
    private val notificationApi: NotificationApi,
    private val sessionStore: SessionStore
) {
    companion object {
        private const val TAG = "TimerRepository"
    }

    private val warningMinutes = 90
    private val limitMinutes = 120
    private val thresholdEvaluator = TimerThresholdEvaluator()

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
        Log.i(TAG, "[START] Creating session at $now, aligner=$alignerNumber")
        timerDao.upsertSession(
            NonWearSessionEntity(
                sessionId = UUID.randomUUID().toString(),
                alignerNumber = alignerNumber,
                startEpochMillis = now,
                endEpochMillis = null,
                dateEpochDay = TimeUtils.epochDayFromMillis(now)
            )
        )
        Log.i(TAG, "[START] Session persisted successfully")
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
        
        // Immediate sync of the session just finished
        try {
            Log.d(TAG, "[STOP] Syncing finished session to backend")
            timerApi.syncSession(
                TimerSessionRequest(
                    sessionId = active.sessionId,
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

        try {
            Log.d(TAG, "[STOP] Fetching summary for average wear hours")
            val response = timerApi.getSummary(includeDaily = false)
            response.averageDailyWearHours?.let {
                sessionStore.saveAverageWearHours(it, response.averageDailyWearDisplay)
                Log.i(TAG, "[STOP] Updated avg wear hours in SessionStore: $it, display: ${response.averageDailyWearDisplay}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "[STOP] Failed to fetch/store avg wear hours", e)
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

            unsynced.forEach { session ->
                Log.d(TAG, "[SYNC_PENDING] Syncing session=${session.sessionId}")
                timerApi.syncSession(
                    TimerSessionRequest(
                        sessionId = session.sessionId,
                        alignerNumber = session.alignerNumber,
                        startEpochMillis = session.startEpochMillis,
                        endEpochMillis = session.endEpochMillis
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
            val today = TimeUtils.todayEpochDay()
            val finishedMillis = timerDao.getDayTotalMillis(today)
            val totalNonWearMinutes = ((finishedMillis + elapsedMillis) / 60000).toInt()

            // 1. Update DB FIRST to "claim" this milestone and prevent double-dispatch
            timerDao.updateLastNotification(activeSession.sessionId, nextInterval)
            Log.d(TAG, "[DISPATCH_CHECK][$source] Updated lastNotificationMinutes=$nextInterval in DB (Pre-dispatch)")

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
            
            Log.i(TAG, "[DISPATCH_CHECK][$source] Dispatch success for milestone=$nextInterval")
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch 422/Error: $errorBody")
            }
            Log.e(TAG, "[DISPATCH_CHECK][$source] Dispatch FAILED for milestone=$nextInterval. Milestone remains marked as notified to avoid spam.", e)
        }
    }
}
