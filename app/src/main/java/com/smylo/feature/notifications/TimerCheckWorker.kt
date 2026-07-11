package com.smylo.feature.notifications

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.smylo.core.common.TimeUtils
import com.smylo.core.preferences.SessionStore
import com.smylo.feature.timer.data.TimerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class TimerCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val timerRepository: TimerRepository,
    private val sessionStore: SessionStore
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        private const val TAG = "TimerCheckWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "[WORKER] Started periodic check")
        try {
            // 1. Sync pending sessions to backend
            timerRepository.syncPendingSessions()

            // 2. Send daily reminder notification only once per day
            val today = TimeUtils.todayEpochDay()
            val lastDaily = sessionStore.lastDailyReminderDay.first()
            val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            
            if (lastDaily < today && currentHour >= 20) {
                Log.i(TAG, "[WORKER] 🔔 TRIGGERING LOCAL NOTIFICATION: Daily summary reminder for day=$today")
                NotificationHelper.createChannels(applicationContext)
                NotificationHelper.send(
                    context = applicationContext,
                    id = 2001,
                    channel = NotificationHelper.CHANNEL_REMINDER,
                    title = "Smylo",
                    body = "Daily summary: Open app to review your non-wear time."
                )
                sessionStore.saveLastDailyReminderDay(today)
                Log.i(TAG, "[WORKER] ✅ LOCAL NOTIFICATION SENT: Daily summary for day=$today")
            } else {
                Log.d(TAG, "[WORKER] Daily summary already sent for day=$today")
            }

            Log.d(TAG, "[WORKER] Completed successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "[WORKER] Failed", e)
            return Result.retry()
        }
        return Result.success()
    }
}

