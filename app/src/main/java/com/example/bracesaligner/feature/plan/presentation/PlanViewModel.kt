package com.example.bracesaligner.feature.plan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.common.AlignerScheduleItem
import com.example.bracesaligner.feature.plan.data.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanUiState(
    val alignerCount: Int = 14,
    val daysPerAligner: Int = 7,
    val loading: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val scheduleItems: List<AlignerScheduleItem> = emptyList()
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: PlanRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    fun updateAlignerCount(value: Int) {
        _uiState.update { it.copy(alignerCount = value.coerceIn(1, 30)) }
    }

    fun updateDaysPerAligner(value: Int) {
        _uiState.update { it.copy(daysPerAligner = value.coerceIn(1, 99)) }
    }

    fun fetchSchedule() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                val schedule = repository.getRemoteSchedule()
                _uiState.update { it.copy(loading = false, scheduleItems = schedule) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.localizedMessage) }
            }
        }
    }

    fun createPlan() {
        viewModelScope.launch {
            runCatching {
                _uiState.update { it.copy(loading = true, error = null) }
                repository.createPlan(_uiState.value.alignerCount, _uiState.value.daysPerAligner)
            }.onSuccess {
                _uiState.update { it.copy(loading = false, saved = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, error = throwable.localizedMessage) }
            }
        }
    }
}
