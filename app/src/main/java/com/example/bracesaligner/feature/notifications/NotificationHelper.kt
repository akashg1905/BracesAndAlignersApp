package com.example.bracesaligner.feature.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    const val CHANNEL_TIMER = "timer_channel"
    const val CHANNEL_REMINDER = "reminder_channel"
    const val CHANNEL_SERVICE = "service_channel"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        
        // 1. Service Channel (Silent/Low importance) for the persistent notification
        manager?.createNotificationChannel(
            NotificationChannel(CHANNEL_SERVICE, "Timer Service", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Keeps the timer running in the background"
                setShowBadge(false)
            }
        )

        // 2. Timer Alerts (High importance) for milestones
        manager?.createNotificationChannel(
            NotificationChannel(CHANNEL_TIMER, "Timer Alerts", NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
        )

        // 3. Daily Reminders (High importance)
        manager?.createNotificationChannel(
            NotificationChannel(CHANNEL_REMINDER, "Daily Reminders", NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
        )
    }

    fun send(context: Context, id: Int, channel: String, title: String, body: String) {
        android.util.Log.i("NotificationHelper", "🔔 SENDING NOTIFICATION [id=$id, channel=$channel]: $title - $body")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            android.util.Log.w("NotificationHelper", "⚠️ Aborting notification: POST_NOTIFICATIONS permission not granted")
            return
        }

        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }
}
