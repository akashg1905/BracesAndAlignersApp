package com.smylo.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.core.database.entity.DailyNonWearSummaryEntity
import com.smylo.core.common.TimeUtils
import com.smylo.core.database.entity.NonWearSessionEntity
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.feature.timer.data.TimerRepository
import com.smylo.feature.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class TimerUiState(
    val planAvailable: Boolean = false,
    val isPlanExpired: Boolean = false,
    val todaySessions: List<NonWearSessionEntity> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository
) : ViewModel() {
    val timerState = timerRepository.observeTimerState()
        .flatMapLatest { state ->
            if (state.isRunning) {
                flow {
                    while (true) {
                        val currentSessionMillis = if (state.activeSessionStart != null) {
                            (TimeUtils.nowMillis() - state.activeSessionStart).coerceAtLeast(0L)
                        } else 0L
                        emit(state.copy(todayTotalMillis = state.todayTotalMillis + currentSessionMillis))
                        delay(1000)
                    }
                }
            } else {
                flowOf(state)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.smylo.core.common.TimerState())

    val weeklySummary = timerRepository.observeWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<DailyNonWearSummaryEntity>())

    val todaySessions = timerRepository.observeSessionsForDay(TimeUtils.todayEpochDay())
        .flatMapLatest { sessions ->
            val hasOngoing = sessions.any { it.endEpochMillis == null }
            if (hasOngoing) {
                flow {
                    while (true) {
                        emit(sessions)
                        delay(1000)
                    }
                }
            } else {
                flowOf(sessions)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDate = MutableStateFlow(TimeUtils.todayEpochDay() - 1)
    val selectedDate = _selectedDate.asStateFlow()

    val availableDays = timerRepository.observeAvailableDays()
        .map { days -> days.filter { it < TimeUtils.todayEpochDay() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedDateSessions = _selectedDate.flatMapLatest { date ->
        timerRepository.observeSessionsForDay(date)
            .flatMapLatest { sessions ->
                val isToday = date == TimeUtils.todayEpochDay()
                val hasOngoing = sessions.any { it.endEpochMillis == null }
                if (isToday && hasOngoing) {
                    flow {
                        while (true) {
                            emit(sessions)
                            delay(1000)
                        }
                    }
                } else {
                    flowOf(sessions)
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile()
                _profileImageUrl.update { profile.profileImage }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Failed to fetch profile", e)
            }
        }

        viewModelScope.launch {
            planRepository.observePlan().collect { plan ->
                _uiState.update { it.copy(
                    planAvailable = plan != null,
                    isPlanExpired = plan?.isExpired ?: false
                ) }
            }
        }
    }

    fun startTimer() = viewModelScope.launch { timerRepository.startTimer() }
    fun stopTimer() = viewModelScope.launch { timerRepository.stopTimer() }

    fun selectDate(epochDay: Long) {
        _selectedDate.value = epochDay
    }

    fun addManualSession(startMillis: Long, endMillis: Long) = viewModelScope.launch {
        timerRepository.addManualSession(startMillis, endMillis)
    }
}

