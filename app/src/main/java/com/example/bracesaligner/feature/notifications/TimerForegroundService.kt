package com.example.bracesaligner.feature.notifications

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bracesaligner.feature.timer.data.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService : Service() {

    @Inject
    lateinit var timerRepository: TimerRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var timerJob: Job? = null

    companion object {
        private const val TAG = "TimerForegroundService"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        NotificationHelper.createChannels(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service starting...")
        // Start with a neutral notification until we fetch the exact start time
        startForeground(NOTIFICATION_ID, createNotification("Timer Active", "Monitoring non-wear time"))
        
        startTimerLoop()
        
        return START_STICKY
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            // Update notification once when starting to show the start time
            updateNotification()
            
            while (isActive) {
                try {
                    Log.i(TAG, "⏱️ Timer Service Heartbeat: Checking milestones...")
                    timerRepository.checkAndDispatchNonWearNotifications(source = "foreground_service")
                    timerRepository.syncPendingSessions()
                    
                    // We no longer update the notification content every minute
                    // to avoid "pinging" the user. The start time is static.
                } catch (e: Exception) {
                    Log.e(TAG, "Loop failed", e)
                }
                delay(60_000)
            }
        }
    }

    private fun updateNotification() {
        serviceScope.launch {
            try {
                val state = timerRepository.observeTimerState().first()
                if (state.isRunning && state.activeSessionStart != null) {
                    val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
                    val startTimeStr = sdf.format(java.util.Date(state.activeSessionStart))
                    
                    val content = "Non-wear session started at $startTimeStr"
                    
                    val notification = createNotification("Timer Active", content)
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update notification", e)
            }
        }
    }

    private fun createNotification(title: String, content: String): Notification {
        return NotificationCompat.Builder(this, NotificationHelper.CHANNEL_SERVICE)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(content)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Service destroyed")
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
