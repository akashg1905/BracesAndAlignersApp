package com.smylo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.smylo.feature.auth.data.TokenRefreshWorker
import com.smylo.feature.errors.data.ClientErrorRepository
import com.smylo.feature.notifications.TimerCheckWorker
import java.util.concurrent.TimeUnit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BracesApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var clientErrorRepository: ClientErrorRepository

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        
        val workManager = WorkManager.getInstance(this)

        // Schedule the timer check worker as a periodic backup (every 15 minutes)
        val timerCheckRequest = PeriodicWorkRequestBuilder<TimerCheckWorker>(15, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "timer_check_worker_periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            timerCheckRequest
        )

        // Schedule the token refresh worker to run once a day
        val tokenRefreshRequest = PeriodicWorkRequestBuilder<TokenRefreshWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "token_refresh_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            tokenRefreshRequest
        )

        clientErrorRepository.enqueueSync()
    }
}

