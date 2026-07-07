package com.smylo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.smylo.core.common.ErrorMessenger
import com.smylo.core.preferences.SessionStore
import com.smylo.feature.notifications.NotificationHelper
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionStore: SessionStore

    @Inject
    lateinit var errorMessenger: ErrorMessenger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        NotificationHelper.createChannels(this)
        requestNotificationPermissionIfNeeded()
        fetchAndStoreFcmToken()
        setContent {
            App(errorMessenger = errorMessenger)
        }
    }

    private fun fetchAndStoreFcmToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    CoroutineScope(Dispatchers.IO).launch {
                        sessionStore.saveFcmToken(token)
                    }
                } else {
                    android.util.Log.e("MainActivity", "FCM token fetch failed", task.exception)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Firebase initialization failed", e)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1100)
    }
}
