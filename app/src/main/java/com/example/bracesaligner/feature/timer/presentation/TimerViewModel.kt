package com.example.bracesaligner.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.feature.timer.data.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : ViewModel() {
    val timerState = timerRepository.observeTimerState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.example.bracesaligner.core.common.TimerState())

    val weeklySummary = timerRepository.observeWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<DailyNonWearSummaryEntity>())

    fun startTimer() = viewModelScope.launch { timerRepository.startTimer() }
    fun stopTimer() = viewModelScope.launch { timerRepository.stopTimer() }
}
