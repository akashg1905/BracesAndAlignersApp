package com.example.bracesaligner.feature.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.bracesaligner.feature.timer.data.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerAlarmReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var timerRepository: TimerRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i(TAG, "onReceive: action=$action")
        
        if (action == ACTION_CHECK_TIMER) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            val wakeLock = powerManager.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "BracesAligner:TimerWakeLock")
            wakeLock.acquire(10 * 1000L) // 10 seconds max

            scope.launch {
                try {
                    Log.d(TAG, "Starting background check from AlarmManager")
                    timerRepository.checkAndDispatchNonWearNotifications(source = "alarm_manager")
                    
                    // Reschedule next check
                    timerRepository.scheduleNextAlarm(context)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in TimerAlarmReceiver", e)
                } finally {
                    if (wakeLock.isHeld) wakeLock.release()
                }
            }
        }
    }

    companion object {
        private const val TAG = "TimerAlarmReceiver"
        const val ACTION_CHECK_TIMER = "com.example.bracesaligner.CHECK_TIMER"
    }
}
