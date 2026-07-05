package com.smylo.feature.dashboard.presentation

import android.util.Log
import com.smylo.core.preferences.SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.core.common.AlignerScheduleItem
import com.smylo.core.common.AlignerPlan
import com.smylo.core.common.TimeUtils
import com.smylo.core.common.TimerState
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.feature.timer.data.TimerRepository
import com.smylo.feature.profile.data.UserRepository
import com.smylo.core.network.dto.UserProfileResponse
import com.smylo.feature.auth.data.AuthRepository
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

import java.text.SimpleDateFormat
import java.util.Date
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
    val isSyncing: Boolean = false,
    val timerState: TimerState = TimerState(),
    val startDateDisplay: String = "",
    val estFinishDateDisplay: String = "",
    val motivationalText: String = "",
    val upcomingTrayDateDisplay: String = ""
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
    ) { plan, timer, loggedIn, avg, avgDisplay ->
        Quintuple(plan, timer, loggedIn, avg, avgDisplay)
    }.combine(userRepository.profileFlow) { quint, profile ->
        Sextuple(quint.first, quint.second, quint.third, quint.fourth, quint.fifth, profile)
    }.flatMapLatest { sextuple: Sextuple<AlignerPlan?, TimerState, Boolean, Double, String, UserProfileResponse?> ->
        val (plan, timerState, isLoggedIn, avgWear, avgWearDisplay, profile) = sextuple
        val scheduleFlow = if (plan != null) {
            planRepository.observeSchedule(plan.planId)
        } else {
            flow { emit(emptyList<AlignerScheduleItem>()) }
        }
        val streakFlow = timerRepository.observeStreakDays(earliestDay = plan?.startDateEpochDay)

        combine(scheduleFlow, streakFlow) { schedule, streakDays ->
            schedule to streakDays
        }.flatMapLatest { (schedule, streakDays) ->
            if (timerState.isRunning) {
                flow {
                    while (true) {
                        emit(buildDashboardState(plan, schedule, timerState, isLoggedIn, avgWear, avgWearDisplay, streakDays, profile))
                        delay(1000)
                    }
                }
            } else {
                flow {
                    emit(buildDashboardState(plan, schedule, timerState, isLoggedIn, avgWear, avgWearDisplay, streakDays, profile))
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            val existingPlan = planRepository.observePlan().first()
            
            // Sync user profile first
            try {
                userRepository.getUserProfile(force = true)
            } catch (e: Exception) {
                Log.e(TAG, "Profile sync failed", e)
            }

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
            _uiState.update { it.copy(isSyncing = false) }
            Log.d(TAG, "[INIT] Sync finished")
        }
        viewModelScope.launch {
            dashboardFlow.collect { updatedState ->
                _uiState.value = updatedState
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
        try {
            userRepository.getUserProfile(force = true)
            planRepository.syncActivePlan()
            if (planRepository.observePlan().first() != null) {
                timerRepository.refreshSummary(force = true)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh failed", e)
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
        avgWearDisplay: String,
        streakDays: Int = 0,
        profile: UserProfileResponse? = null
    ): DashboardUiState {
        // Calculate formatted non-wear time including seconds
        var currentSessionMillis = 0L
        if (timer.isRunning && timer.activeSessionStart != null) {
            currentSessionMillis = TimeUtils.nowMillis() - timer.activeSessionStart
        }
        val totalMillis = timer.todayTotalMillis + currentSessionMillis
        val formattedTime = TimeUtils.formatDurationHMS(totalMillis)
        val greeting = TimeUtils.getGreeting()

        val isRefreshing = _uiState.value.isRefreshing
        val userName = profile?.firstName ?: "Patient"
        val pImageUrl = profile?.profileImage

        if (plan == null) return DashboardUiState(
            greeting = greeting,
            userName = userName,
            profileImageUrl = pImageUrl,
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
                userName = userName,
                profileImageUrl = pImageUrl,
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
            ?: return DashboardUiState(greeting = greeting, userName = userName, profileImageUrl = pImageUrl, planAvailable = true, isLoggedIn = isLoggedIn)
        
        val actualDaysLeft = (active.endEpochDay - today).toInt()
        val progress = (active.alignerNumber.toFloat() / plan.alignerCount.toFloat()).coerceIn(0f, 1f)
        
        // Progress within the current aligner
        val totalDaysInAligner = (active.endEpochDay - active.startEpochDay + 1).toFloat()
        val daysPassedInAligner = (today - active.startEpochDay).toFloat()
        val currentAlignerProgress = if (totalDaysInAligner > 0) {
            (daysPassedInAligner / totalDaysInAligner).coerceIn(0f, 1f)
        } else 0f

        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        
        val firstAligner = schedule.minByOrNull { it.alignerNumber }
        val lastAligner = schedule.maxByOrNull { it.alignerNumber }

        val startDateEpoch = firstAligner?.startEpochDay ?: plan.startDateEpochDay
        val startDateDisplay = dateFormat.format(Date(startDateEpoch * 24 * 60 * 60 * 1000L))
        
        val finishDateEpoch = lastAligner?.endEpochDay ?: (plan.startDateEpochDay + (plan.alignerCount * (if (plan.daysPerAligner > 0) plan.daysPerAligner else 14)))
        val estFinishDateDisplay = dateFormat.format(Date(finishDateEpoch * 24 * 60 * 60 * 1000L))

        val motivationalText = when {
            progress < 0.2f -> "You're just starting your journey. Exciting times ahead!"
            progress < 0.4f -> "Great progress! You're moving steadily towards your goal."
            progress < 0.6f -> "You're exactly halfway through your treatment. Your smile transformation is progressing perfectly."
            progress < 0.8f -> "You're in the home stretch! Keep up the great work."
            else -> "Almost there! Your new smile is just around the corner."
        }
        
        val traySdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        val upcomingTrayDateDisplay = traySdf.format(Date(active.endEpochDay * 24 * 60 * 60 * 1000L))

        return DashboardUiState(
            greeting = greeting,
            userName = userName,
            profileImageUrl = pImageUrl,
            totalTransformationProgress = (progress * 100).toInt(),
            currentAlignerNumber = active.alignerNumber,
            totalAligners = plan.alignerCount,
            daysLeftInCurrentAligner = actualDaysLeft.coerceAtLeast(0),
            nextAlignerNumber = if (active.alignerNumber < plan.alignerCount) active.alignerNumber + 1 else null,
            currentAlignerProgress = currentAlignerProgress,
            averageDailyHours = avgWear.toFloat(),
            averageDailyWearDisplay = avgWearDisplay,
            nonWearTimeTodayFormatted = formattedTime,
            streakDays = streakDays,
            proTip = "Rinse your aligners with lukewarm water every time you remove them to prevent buildup.",
            isScanRequired = true,
            planAvailable = true,
            isPlanExpired = false,
            isLoggedIn = isLoggedIn,
            isRefreshing = isRefreshing,
            timerState = timer,
            startDateDisplay = startDateDisplay,
            estFinishDateDisplay = estFinishDateDisplay,
            motivationalText = motivationalText,
            upcomingTrayDateDisplay = upcomingTrayDateDisplay
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

data class Sextuple<A, B, C, D, E, F>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F)

