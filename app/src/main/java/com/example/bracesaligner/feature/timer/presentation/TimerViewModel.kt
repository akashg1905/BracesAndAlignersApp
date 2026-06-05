package com.example.bracesaligner.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.feature.timer.data.TimerRepository
import com.example.bracesaligner.feature.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val timerState = timerRepository.observeTimerState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.example.bracesaligner.core.common.TimerState())

    val weeklySummary = timerRepository.observeWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<DailyNonWearSummaryEntity>())

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile()
                _profileImageUrl.update { profile.profileImage }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Failed to fetch profile", e)
            }
        }
    }

    fun startTimer() = viewModelScope.launch { timerRepository.startTimer() }
    fun stopTimer() = viewModelScope.launch { timerRepository.stopTimer() }
}
