package com.example.bracesaligner.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity
import com.example.bracesaligner.feature.plan.data.PlanRepository
import com.example.bracesaligner.feature.timer.data.TimerRepository
import com.example.bracesaligner.feature.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

data class TimerUiState(
    val planAvailable: Boolean = false,
    val isPlanExpired: Boolean = false,
    val todaySessions: List<NonWearSessionEntity> = emptyList()
)

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository
) : ViewModel() {
    val timerState = timerRepository.observeTimerState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.example.bracesaligner.core.common.TimerState())

    val weeklySummary = timerRepository.observeWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<DailyNonWearSummaryEntity>())

    val todaySessions = timerRepository.observeSessionsForDay(TimeUtils.todayEpochDay())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
}
