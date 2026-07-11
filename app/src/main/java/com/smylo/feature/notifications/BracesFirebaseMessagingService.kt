package com.smylo.feature.notifications

import com.smylo.core.preferences.SessionStore
import com.smylo.feature.auth.data.AuthRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BracesFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var sessionStore: SessionStore

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            sessionStore.saveFcmToken(token)
            // Only try to register on backend if we have a session
            if (authRepository.isLoggedIn()) {
                authRepository.registerDeviceToken(token)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // Log the entire raw data to help debug exactly what the backend is sending
        android.util.Log.i("SmyloFCM", "📩 FCM RAW DATA: ${message.data}")
        android.util.Log.i("SmyloFCM", "📩 FCM NOTIFICATION: title='${message.notification?.title}', body='${message.notification?.body}'")

        // 1. Try to get title from various possible keys
        val title = message.data["title"] 
            ?: message.data["notification_title"]
            ?: message.notification?.title 
            ?: "Smylo"
        
        // 2. Try to get body/message from various possible keys
        val body = message.data["body"]
            ?: message.data["message"]
            ?: message.data["msg"]
            ?: message.data["text"]
            ?: message.data["alert"]
            ?: message.data["notification_body"]
            ?: message.notification?.body
            ?: "Time for a check! Please check your aligner status." // More descriptive fallback

        android.util.Log.i("SmyloFCM", "🔔 DISPATCHING: Title='$title', Body='$body'")
        
        NotificationHelper.createChannels(this)
        NotificationHelper.send(
            context = this,
            id = 3001,
            channel = NotificationHelper.CHANNEL_REMINDER,
            title = title,
            body = body
        )
    }
}

