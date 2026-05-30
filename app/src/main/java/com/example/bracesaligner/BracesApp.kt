package com.example.bracesaligner

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.bracesaligner.feature.auth.data.TokenRefreshWorker
import com.example.bracesaligner.feature.notifications.TimerCheckWorker
import java.util.concurrent.TimeUnit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BracesApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        
        val workManager = WorkManager.getInstance(this)

        // Schedule the timer check worker to run every 5 minutes (using OneTimeWork chain)
        val timerCheckRequest = OneTimeWorkRequestBuilder<TimerCheckWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniqueWork(
            "timer_check_worker_oneshot",
            ExistingWorkPolicy.KEEP,
            timerCheckRequest
        )

        // Schedule the token refresh worker to run once a day
        val tokenRefreshRequest = PeriodicWorkRequestBuilder<TokenRefreshWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "token_refresh_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            tokenRefreshRequest
        )
    }
}
