package com.example.bracesaligner.feature.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BracesFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: send token to backend via notification API endpoint.
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
