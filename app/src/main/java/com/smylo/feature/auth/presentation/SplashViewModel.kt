package com.smylo.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.feature.auth.data.AuthRepository
import com.smylo.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.feature.timer.data.TimerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val planRepository: PlanRepository,
    private val timerRepository: TimerRepository
) : ViewModel() {
    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1200)
            val isLoggedIn = authRepository.observeLoggedIn().first()
            if (isLoggedIn) {
                try {
                    // Sync the latest plan state from backend
                    planRepository.syncActivePlan()
                    val plan = planRepository.observePlan().first()
                    
                    if (plan == null) {
                        _destination.value = Routes.PLAN_SETUP
                    } else {
                        // Restore sessions and timer state from backend
                        timerRepository.refreshSummary()
                        _destination.value = Routes.DASHBOARD
                    }
                } catch (e: Exception) {
                    // Fallback to Dashboard if sync fails, it will handle no-plan state
                    _destination.value = Routes.DASHBOARD
                }
            } else {
                _destination.value = Routes.AUTH
            }
        }
    }
}

