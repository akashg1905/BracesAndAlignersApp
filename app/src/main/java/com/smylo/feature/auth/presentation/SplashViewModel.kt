package com.smylo.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.feature.auth.data.AuthRepository
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val planRepository: PlanRepository
) : ViewModel() {
    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1200)

            val isLoggedIn = withTimeoutOrNull(5_000) {
                authRepository.observeLoggedIn().first()
            } ?: authRepository.isLoggedIn()

            if (!isLoggedIn) {
                _destination.value = Routes.AUTH_LOGIN
                return@launch
            }

            // Navigate from local data only — network sync runs on Dashboard/Auth, not here.
            // Blocking on syncActivePlan + refreshSummary could stall splash for minutes on slow networks.
            val plan = withTimeoutOrNull(3_000) {
                planRepository.observePlan().first()
            }
            _destination.value = if (plan == null) Routes.PLAN_SETUP else Routes.DASHBOARD
        }
    }
}
