package com.example.bracesaligner.feature.dashboard.presentation

import com.example.bracesaligner.core.preferences.SessionStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.common.AlignerPlan
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.feature.plan.data.PlanRepository
import com.example.bracesaligner.feature.plan.domain.ScheduleGenerator
import com.example.bracesaligner.feature.timer.data.TimerRepository
import com.example.bracesaligner.feature.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

import java.util.Locale

data class DashboardUiState(
    val userName: String = "Patient",
    val totalTransformationProgress: Int = 0,
    val currentAlignerNumber: Int = 0,
    val totalAligners: Int = 0,
    val daysLeftInCurrentAligner: Int = 0,
    val nextAlignerNumber: Int? = null,
    val currentAlignerProgress: Float = 0f,
    val averageDailyHours: Float = 0f,
    val nonWearTimeTodayFormatted: String = "00:00:00",
    val nextCheckUpDate: String? = null,
    val streakDays: Int = 0,
    val proTip: String = "",
    val isScanRequired: Boolean = false,
    val planAvailable: Boolean = false,
    val isLoggedIn: Boolean = true,
    val isRefreshing: Boolean = false,
    val timerState: TimerState = TimerState()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val timerRepository: TimerRepository,
    private val scheduleGenerator: ScheduleGenerator,
    private val authRepository: AuthRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dashboardFlow = combine(
        planRepository.observePlan(),
        timerRepository.observeTimerState(),
        authRepository.observeLoggedIn(),
        sessionStore.averageWearHours
    ) { plan, timerState, isLoggedIn, avgWear ->
        Quadruple(plan, timerState, isLoggedIn, avgWear)
    }.flatMapLatest { (plan, timerState, isLoggedIn, avgWear) ->
        if (timerState.isRunning) {
            flow {
                while (true) {
                    emit(buildDashboardState(plan, timerState, isLoggedIn, avgWear))
                    delay(1000)
                }
            }
        } else {
            flow {
                emit(buildDashboardState(plan, timerState, isLoggedIn, avgWear))
            }
        }
    }

    init {
        viewModelScope.launch {
            dashboardFlow.collect { _uiState.value = it }
        }
    }

    fun startTimer() = viewModelScope.launch { timerRepository.startTimer() }
    fun stopTimer() = viewModelScope.launch { timerRepository.stopTimer() }
    fun logout() = viewModelScope.launch { authRepository.logout() }
    
    fun refresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        planRepository.syncActivePlan()
        _uiState.value = _uiState.value.copy(isRefreshing = false)
    }

    private fun buildDashboardState(plan: AlignerPlan?, timer: TimerState, isLoggedIn: Boolean, avgWear: Double): DashboardUiState {
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

        val isRefreshing = _uiState.value.isRefreshing

        if (plan == null) return DashboardUiState(
            timerState = timer, 
            planAvailable = false, 
            isLoggedIn = isLoggedIn,
            nonWearTimeTodayFormatted = formattedTime,
            isRefreshing = isRefreshing,
            averageDailyHours = avgWear.toFloat()
        )
        
        val schedule = scheduleGenerator.generate(plan.alignerCount, plan.daysPerAligner, plan.startDateEpochDay)
        val today = TimeUtils.todayEpochDay()
        val active = schedule.firstOrNull { today in it.startEpochDay..it.endEpochDay } ?: schedule.last()
        
        val daysLeft = (active.endEpochDay - today).toInt().coerceAtLeast(0)
        val progress = (active.alignerNumber.toFloat() / plan.alignerCount.toFloat()).coerceIn(0f, 1f)
        
        // Progress within the current aligner
        val totalDaysInAligner = (active.endEpochDay - active.startEpochDay + 1).toFloat()
        val daysPassedInAligner = (today - active.startEpochDay).toFloat()
        val currentAlignerProgress = (daysPassedInAligner / totalDaysInAligner).coerceIn(0f, 1f)

        return DashboardUiState(
            userName = "Sarah", // Hardcoded for now as per design
            totalTransformationProgress = (progress * 100).toInt(),
            currentAlignerNumber = active.alignerNumber,
            totalAligners = plan.alignerCount,
            daysLeftInCurrentAligner = daysLeft,
            nextAlignerNumber = if (active.alignerNumber < plan.alignerCount) active.alignerNumber + 1 else null,
            currentAlignerProgress = currentAlignerProgress,
            averageDailyHours = avgWear.toFloat(), 
            nonWearTimeTodayFormatted = formattedTime,
            nextCheckUpDate = "Oct 24 • 10:30 AM",
            streakDays = 12,
            proTip = "Rinse your aligners with lukewarm water every time you remove them to prevent buildup.",
            isScanRequired = true,
            planAvailable = true,
            isLoggedIn = isLoggedIn,
            isRefreshing = isRefreshing,
            timerState = timer
        )
    }

}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
