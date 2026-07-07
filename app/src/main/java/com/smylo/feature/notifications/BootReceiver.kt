package com.smylo.feature.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.smylo.feature.timer.data.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var timerRepository: TimerRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BootReceiver", "Device booted, checking if foreground service needs to restart")
            scope.launch {
                val state = timerRepository.observeTimerState().first()
                if (state.isRunning) {
                    Log.i("BootReceiver", "Active session found, starting TimerForegroundService")
                    val serviceIntent = Intent(context, TimerForegroundService::class.java)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                } else {
                    Log.d("BootReceiver", "No active session, service not started")
                }
            }
        }
    }
}

