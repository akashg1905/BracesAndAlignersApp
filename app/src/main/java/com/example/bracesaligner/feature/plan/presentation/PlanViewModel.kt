package com.example.bracesaligner.feature.plan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.core.common.AlignerScheduleItem
import com.example.bracesaligner.feature.plan.data.PlanRepository
import com.example.bracesaligner.feature.profile.data.UserRepository
import com.example.bracesaligner.core.network.dto.UpdateAlignerRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import android.util.Log
import javax.inject.Inject

import java.time.LocalDate

data class PlanUiState(
    val userName: String = "Patient",
    val profileImageUrl: String? = null,
    val alignerCount: Int = 14,
    val daysPerAligner: Int = 7,
    val startDate: LocalDate = LocalDate.now(),
    val loading: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val scheduleItems: List<AlignerScheduleItem> = emptyList(),
    val originalScheduleItems: List<AlignerScheduleItem> = emptyList(),
    val planId: String? = null,
    val isUpdating: Boolean = false
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: PlanRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observePlan().collectLatest { plan ->
                if (plan != null) {
                    _uiState.update { it.copy(
                        planId = plan.planId,
                        alignerCount = plan.alignerCount,
                        daysPerAligner = plan.daysPerAligner,
                        startDate = LocalDate.ofEpochDay(plan.startDateEpochDay)
                    ) }
                    
                    // Observe schedule from DB
                    launch {
                        repository.observeSchedule(plan.planId).collect { schedule ->
                            _uiState.update { it.copy(
                                scheduleItems = schedule,
                                originalScheduleItems = schedule // Keep track to detect changes
                            ) }
                        }
                    }
                }
            }
        }
        
        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile()
                _uiState.update { state ->
                    state.copy(
                        userName = profile.firstName ?: "Patient",
                        profileImageUrl = profile.profileImage
                    )
                }
            } catch (e: Exception) {
                Log.e("PlanViewModel", "Failed to fetch profile", e)
            }
        }
    }

    fun updateAlignerCount(value: Int) {
        _uiState.update { it.copy(alignerCount = value) }
    }

    fun updateDaysPerAligner(value: Int) {
        _uiState.update { it.copy(daysPerAligner = value) }
    }

    fun updateStartDate(date: LocalDate) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun fetchSchedule() {
        // Data is now automatically observed from the database in init {}
    }

    fun incrementDaysForAligner(alignerId: String) {
        _uiState.update { state ->
            val updatedList = state.scheduleItems.map { item ->
                if (item.id == alignerId) {
                    item.copy(daysForAligner = item.daysForAligner + 1)
                } else {
                    item
                }
            }
            state.copy(scheduleItems = updatedList)
        }
    }

    fun decrementDaysForAligner(alignerId: String) {
        _uiState.update { state ->
            val updatedList = state.scheduleItems.map { item ->
                if (item.id == alignerId && item.daysForAligner > 1) {
                    item.copy(daysForAligner = item.daysForAligner - 1)
                } else {
                    item
                }
            }
            state.copy(scheduleItems = updatedList)
        }
    }

    fun updateSchedule() {
        val state = _uiState.value
        val updates = state.scheduleItems.filterIndexed { index, item ->
            val original = state.originalScheduleItems.getOrNull(index)
            original != null && item.daysForAligner != original.daysForAligner
        }.map {
            UpdateAlignerRequest(it.id, it.daysForAligner)
        }

        if (updates.isEmpty()) return

        viewModelScope.launch {
            runCatching {
                _uiState.update { it.copy(isUpdating = true, error = null) }
                repository.updateSchedule(state.planId, updates)
            }.onSuccess {
                _uiState.update { it.copy(isUpdating = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isUpdating = false, error = throwable.localizedMessage) }
            }
        }
    }

    fun createPlan() {
        viewModelScope.launch {
            runCatching {
                _uiState.update { it.copy(loading = true, error = null) }
                repository.createPlan(
                    alignerCount = _uiState.value.alignerCount,
                    daysPerAligner = _uiState.value.daysPerAligner,
                    startEpochDay = _uiState.value.startDate.toEpochDay()
                )
            }.onSuccess {
                _uiState.update { it.copy(loading = false, saved = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, error = throwable.localizedMessage) }
            }
        }
    }
}
