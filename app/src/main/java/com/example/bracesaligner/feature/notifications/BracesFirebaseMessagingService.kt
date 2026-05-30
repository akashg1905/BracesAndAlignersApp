package com.example.bracesaligner.feature.notifications

import com.example.bracesaligner.core.preferences.SessionStore
import com.example.bracesaligner.feature.auth.data.AuthRepository
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
        NotificationHelper.createChannels(this)
        NotificationHelper.send(
            context = this,
            id = 3001,
            channel = NotificationHelper.CHANNEL_REMINDER,
            title = message.notification?.title ?: "BracesAndAligner",
            body = message.notification?.body ?: "You have a new reminder."
        )
    }
}
