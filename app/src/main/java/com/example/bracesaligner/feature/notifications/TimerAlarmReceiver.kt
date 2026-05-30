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
            scope.launch {
                try {
                    Log.d(TAG, "Starting background check from AlarmManager")
                    timerRepository.checkAndDispatchNonWearNotifications(source = "alarm_manager")
                    
                    // Reschedule next check if session is still active
                    // Note: TimerRepository will handle the calculation of the next milestone
                    timerRepository.scheduleNextAlarm(context)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in TimerAlarmReceiver", e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "TimerAlarmReceiver"
        const val ACTION_CHECK_TIMER = "com.example.bracesaligner.CHECK_TIMER"
    }
}
