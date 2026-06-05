package com.example.bracesaligner.feature.notifications

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.preferences.SessionStore
import com.example.bracesaligner.feature.timer.data.TimerRepository
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
                Log.d(TAG, "[WORKER] Sending daily summary reminder for day=$today")
                NotificationHelper.createChannels(applicationContext)
                NotificationHelper.send(
                    context = applicationContext,
                    id = 2001,
                    channel = NotificationHelper.CHANNEL_REMINDER,
                    title = "Braces & Aligner",
                    body = "Daily summary: Open app to review your non-wear time."
                )
                sessionStore.saveLastDailyReminderDay(today)
            } else {
                Log.d(TAG, "[WORKER] Daily summary already sent for day=$today")
            }

            Log.d(TAG, "[WORKER] Completed successfully. Rescheduling in 5 minutes.")
            
            // Self-reschedule to bypass the 15-minute minimum of PeriodicWork
            val nextRequest = OneTimeWorkRequestBuilder<TimerCheckWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "timer_check_worker_oneshot",
                ExistingWorkPolicy.REPLACE,
                nextRequest
            )

        } catch (e: Exception) {
            Log.e(TAG, "[WORKER] Failed", e)
            // Even on failure, we want to try again in 5 mins
            val nextRequest = OneTimeWorkRequestBuilder<TimerCheckWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "timer_check_worker_oneshot",
                ExistingWorkPolicy.REPLACE,
                nextRequest
            )
            return Result.retry()
        }
        return Result.success()
    }
}
