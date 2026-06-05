package com.example.bracesaligner.feature.dashboard.presentation

import android.util.Log
import com.example.bracesaligner.core.preferences.SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.common.AlignerScheduleItem
import com.example.bracesaligner.core.common.AlignerPlan
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.feature.plan.data.PlanRepository
import com.example.bracesaligner.feature.timer.data.TimerRepository
import com.example.bracesaligner.feature.profile.data.UserRepository
import com.example.bracesaligner.feature.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

import java.util.Locale

data class DashboardUiState(
    val greeting: String = "Hello",
    val userName: String = "Patient",
    val profileImageUrl: String? = null,
    val totalTransformationProgress: Int = 0,
    val currentAlignerNumber: Int = 0,
    val totalAligners: Int = 0,
    val daysLeftInCurrentAligner: Int = 0,
    val nextAlignerNumber: Int? = null,
    val currentAlignerProgress: Float = 0f,
    val averageDailyHours: Float = 0f,
    val averageDailyWearDisplay: String = "--",
    val nonWearTimeTodayFormatted: String = "00:00:00",
    val nextCheckUpDate: String? = null,
    val streakDays: Int = 0,
    val proTip: String = "",
    val isScanRequired: Boolean = false,
    val planAvailable: Boolean = false,
    /** True when server returned plan with `plan_status` / `planStatus` = expired (after last day). */
    val isPlanExpired: Boolean = false,
    val isLoggedIn: Boolean = true,
    val isRefreshing: Boolean = false,
    val timerState: TimerState = TimerState()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val timerRepository: TimerRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    companion object {
        private const val TAG = "DashboardVM"
    }

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    private var notificationMonitorJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dashboardFlow = combine(
        planRepository.observePlan(),
        timerRepository.observeTimerState(),
        authRepository.observeLoggedIn(),
        sessionStore.averageWearHours,
        sessionStore.averageWearDisplay
    ) { plan, timerState, isLoggedIn, avgWear, avgWearDisplay ->
        Quintuple(plan, timerState, isLoggedIn, avgWear, avgWearDisplay)
    }.flatMapLatest { (plan, timerState, isLoggedIn, avgWear, avgWearDisplay) ->
        val scheduleFlow = if (plan != null) {
            planRepository.observeSchedule(plan.planId)
        } else {
            flow { emit(emptyList<AlignerScheduleItem>()) }
        }

        scheduleFlow.flatMapLatest { schedule ->
            if (timerState.isRunning) {
                flow {
                    while (true) {
                        emit(buildDashboardState(plan, schedule, timerState, isLoggedIn, avgWear, avgWearDisplay))
                        delay(1000)
                    }
                }
            } else {
                flow {
                    emit(buildDashboardState(plan, schedule, timerState, isLoggedIn, avgWear, avgWearDisplay))
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val existingPlan = planRepository.observePlan().first()
            
            // Only sync from network if we don't have a plan or if it's the first load of the session
            if (existingPlan == null) {
                Log.d(TAG, "[INIT] No local plan found, syncing from backend")
                planRepository.syncActivePlan()
            }

            // Only refresh summary if we have a plan
            val plan = planRepository.observePlan().first()
            if (plan != null) {
                timerRepository.refreshSummary()
            }
            Log.d(TAG, "[INIT] Sync finished")
        }
        viewModelScope.launch {
            dashboardFlow.collect { updatedState ->
                _uiState.update { current ->
                    updatedState.copy(
                        userName = current.userName,
                        profileImageUrl = current.profileImageUrl
                    )
                }
                Log.v(TAG, "[STATE] timerRunning=${updatedState.timerState.isRunning} planAvailable=${updatedState.planAvailable} expired=${updatedState.isPlanExpired}")
                updateNotificationMonitor(updatedState.timerState.isRunning)
            }
        }
    }

    fun startTimer() = viewModelScope.launch {
        Log.i(TAG, "[TIMER_UI] Start tapped")
        timerRepository.startTimer()
        updateNotificationMonitor(isRunning = true)
    }

    fun stopTimer() = viewModelScope.launch {
        Log.i(TAG, "[TIMER_UI] Stop tapped")
        timerRepository.stopTimer()
        updateNotificationMonitor(isRunning = false)
    }
    fun logout() = viewModelScope.launch { 
        val token = sessionStore.fcmToken.first()
        authRepository.logout(token) 
    }
    
    fun refresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        planRepository.syncActivePlan()
        if (planRepository.observePlan().first() != null) {
            timerRepository.refreshSummary()
        }
        _uiState.value = _uiState.value.copy(isRefreshing = false)
    }

    private fun updateNotificationMonitor(isRunning: Boolean) {
        if (isRunning) {
            if (notificationMonitorJob?.isActive == true) return
            
            // Instead of a loop, we just do a one-time "catch up" check 
            // when the UI becomes active or timer starts.
            notificationMonitorJob = viewModelScope.launch {
                runCatching { 
                    timerRepository.checkAndDispatchNonWearNotifications(source = "foreground_entry") 
                }.onFailure { Log.e(TAG, "[FG_CHECK] failed", it) }
            }
        } else {
            notificationMonitorJob?.cancel()
            notificationMonitorJob = null
        }
    }

    /**
     * Adaptive polling to balance reliability and battery:
     * - 15s when we are within 1 minute of the next 5-min milestone
     * - 30s when within 2 minutes
     * - 60s otherwise
     */
    private fun computeAdaptiveCheckDelayMillis(): Long {
        val state = _uiState.value.timerState
        if (!state.isRunning) return 60_000L
        val currentSessionMillis = if (state.activeSessionStart != null) {
            (TimeUtils.nowMillis() - state.activeSessionStart).coerceAtLeast(0L)
        } else 0L
        val totalMinutes = ((state.todayTotalMillis + currentSessionMillis) / 60_000L).toInt()
        val minutesUntilNextMilestone = 5 - (totalMinutes % 5)
        val delayMs = when {
            minutesUntilNextMilestone <= 1 -> 15_000L
            minutesUntilNextMilestone <= 2 -> 30_000L
            else -> 60_000L
        }
        Log.v(
            TAG,
            "[FG_MONITOR] elapsedMin=$totalMinutes nextMilestoneInMin=$minutesUntilNextMilestone delayMs=$delayMs"
        )
        return delayMs
    }

    private fun buildDashboardState(
        plan: AlignerPlan?,
        schedule: List<AlignerScheduleItem>,
        timer: TimerState,
        isLoggedIn: Boolean,
        avgWear: Double,
        avgWearDisplay: String
    ): DashboardUiState {
        // Calculate formatted non-wear time including seconds
        var currentSessionMillis = 0L
        if (timer.isRunning && timer.activeSessionStart != null) {
            currentSessionMillis = TimeUtils.nowMillis() - timer.activeSessionStart
        }
        val totalMillis = timer.todayTotalMillis + currentSessionMillis
        val seconds = (totalMillis / 1000) % 60
        val minutes = (totalMillis / (1000 * 60)) % 60
        val hours = (totalMillis / (1000 * 60 * 60))
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        val greeting = TimeUtils.getGreeting()

        val isRefreshing = _uiState.value.isRefreshing

        if (plan == null) return DashboardUiState(
            greeting = greeting,
            timerState = timer,
            planAvailable = false,
            isPlanExpired = false,
            isLoggedIn = isLoggedIn,
            nonWearTimeTodayFormatted = formattedTime,
            isRefreshing = isRefreshing,
            averageDailyHours = avgWear.toFloat(),
            averageDailyWearDisplay = avgWearDisplay
        )

        if (plan.isExpired) {
            return DashboardUiState(
                greeting = greeting,
                userName = "Patient",
                totalTransformationProgress = 100,
                currentAlignerNumber = plan.alignerCount,
                totalAligners = plan.alignerCount,
                daysLeftInCurrentAligner = 0,
                nextAlignerNumber = null,
                currentAlignerProgress = 1f,
                averageDailyHours = avgWear.toFloat(),
                averageDailyWearDisplay = avgWearDisplay,
                nonWearTimeTodayFormatted = formattedTime,
                nextCheckUpDate = null,
                streakDays = 0,
                proTip = "Congratulations on completing your aligner plan. Contact your clinic if you need a refinement or retainer.",
                isScanRequired = false,
                planAvailable = true,
                isPlanExpired = true,
                isLoggedIn = isLoggedIn,
                isRefreshing = isRefreshing,
                timerState = timer
            )
        }

        // Use the server-driven schedule to determine active aligner
        val today = TimeUtils.todayEpochDay()
        val active = schedule.find { it.isCurrent } 
            ?: schedule.firstOrNull { today in it.startEpochDay..it.endEpochDay } 
            ?: schedule.lastOrNull()
            ?: return DashboardUiState(greeting = greeting, planAvailable = true, isLoggedIn = isLoggedIn)
        
        val daysLeft = (active.endEpochDay - today).toInt().coerceAtLeast(0)
        val progress = (active.alignerNumber.toFloat() / plan.alignerCount.toFloat()).coerceIn(0f, 1f)
        
        // Progress within the current aligner
        val totalDaysInAligner = (active.endEpochDay - active.startEpochDay + 1).toFloat()
        val daysPassedInAligner = (today - active.startEpochDay).toFloat()
        val currentAlignerProgress = if (totalDaysInAligner > 0) {
            (daysPassedInAligner / totalDaysInAligner).coerceIn(0f, 1f)
        } else 0f

        return DashboardUiState(
            greeting = greeting,
            userName = "Patient",
            totalTransformationProgress = (progress * 100).toInt(),
            currentAlignerNumber = active.alignerNumber,
            totalAligners = plan.alignerCount,
            daysLeftInCurrentAligner = daysLeft,
            nextAlignerNumber = if (active.alignerNumber < plan.alignerCount) active.alignerNumber + 1 else null,
            currentAlignerProgress = currentAlignerProgress,
            averageDailyHours = avgWear.toFloat(),
            averageDailyWearDisplay = avgWearDisplay,
            nonWearTimeTodayFormatted = formattedTime,
            nextCheckUpDate = "Oct 24 • 10:30 AM",
            streakDays = 12,
            proTip = "Rinse your aligners with lukewarm water every time you remove them to prevent buildup.",
            isScanRequired = true,
            planAvailable = true,
            isPlanExpired = false,
            isLoggedIn = isLoggedIn,
            isRefreshing = isRefreshing,
            timerState = timer
        )
    }

    override fun onCleared() {
        Log.d(TAG, "[LIFECYCLE] onCleared -> cancel monitor")
        notificationMonitorJob?.cancel()
        super.onCleared()
    }

}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

data class Quintuple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
