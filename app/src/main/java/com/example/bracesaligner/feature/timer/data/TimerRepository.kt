package com.example.bracesaligner.feature.timer.data

import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.core.database.dao.AlignerPlanDao
import com.example.bracesaligner.core.database.dao.NonWearTimerDao
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity
import com.example.bracesaligner.core.network.api.TimerApi
import com.example.bracesaligner.core.network.dto.DailySummaryRequest
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
    private val sessionStore: SessionStore
) {
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
        if (timerDao.getActiveSession() != null) return
        val now = TimeUtils.nowMillis()
        timerDao.upsertSession(
            NonWearSessionEntity(
                sessionId = UUID.randomUUID().toString(),
                alignerNumber = getCurrentAlignerNumber(),
                startEpochMillis = now,
                endEpochMillis = null,
                dateEpochDay = TimeUtils.epochDayFromMillis(now)
            )
        )
    }

    suspend fun stopTimer() {
        val active = timerDao.getActiveSession() ?: return
        val now = TimeUtils.nowMillis()
        timerDao.stopSession(active.sessionId, now)
        
        // Immediate sync of the session just finished
        try {
            timerApi.syncSession(
                TimerSessionRequest(
                    sessionId = active.sessionId,
                    alignerNumber = active.alignerNumber,
                    startEpochMillis = active.startEpochMillis,
                    endEpochMillis = now
                )
            )
            timerDao.markAsSynced(listOf(active.sessionId))
        } catch (e: Exception) {
            e.printStackTrace()
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

        try {
            val response = timerApi.getSummary(includeDaily = false)
            response.averageDailyWearHours?.let {
                sessionStore.saveAverageWearHours(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncPendingSessions() {
        try {
            val unsynced = timerDao.getUnsyncedSessions()
            if (unsynced.isEmpty()) return

            unsynced.forEach { session ->
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeWeeklySummary() = timerDao.observeRecentSummary(7)
}
