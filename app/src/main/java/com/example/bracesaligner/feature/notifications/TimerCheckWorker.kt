package com.example.bracesaligner.feature.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bracesaligner.feature.timer.data.TimerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TimerCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val timerRepository: TimerRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // 1. Sync pending sessions to backend (Once a day logic)
        timerRepository.syncPendingSessions()

        // 2. Send reminder notification
        NotificationHelper.createChannels(applicationContext)
        NotificationHelper.send(
            context = applicationContext,
            id = 2001,
            channel = NotificationHelper.CHANNEL_REMINDER,
            title = "BracesAndAligner",
            body = "Daily summary: open app to review your non-wear time."
        )
        return Result.success()
    }
}
