package com.smylo.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.feature.auth.data.AuthRepository
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.feature.timer.data.TimerRepository
import com.smylo.feature.profile.data.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val phoneNumber: String = "",
    val otpCode: String = "",
    val otpRequested: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val planRepository: PlanRepository,
    private val timerRepository: TimerRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeLoggedIn().collectLatest { isLoggedIn ->
                _uiState.update { it.copy(loggedIn = isLoggedIn) }
            }
        }
    }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPhoneNumberChange(value: String) = _uiState.update { it.copy(phoneNumber = value) }
    fun onOtpChange(value: String) = _uiState.update { it.copy(otpCode = value) }

    fun dismissError() = _uiState.update { it.copy(error = null) }

    fun requestOtp() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                repository.sendOtp(_uiState.value.email, _uiState.value.phoneNumber)
                _uiState.update { it.copy(loading = false, otpRequested = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.localizedMessage) }
            }
        }
    }

    fun verifyOtp() {
        if (_uiState.value.loading || _uiState.value.loggedIn) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                repository.verifyOtp(
                    email = _uiState.value.email,
                    phoneNumber = _uiState.value.phoneNumber,
                    code = _uiState.value.otpCode
                )
                // Sync plan and summary after successful login. 
                try {
                    userRepository.syncProfile()
                    planRepository.syncActivePlan()
                    // Fetch summary only if plan is available
                    if (planRepository.observePlan().first() != null) {
                        timerRepository.refreshSummary(force = true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Explicitly update loggedIn to true on success for immediate navigation
                _uiState.update { it.copy(loading = false, loggedIn = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.localizedMessage) }
            }
        }
    }
}

