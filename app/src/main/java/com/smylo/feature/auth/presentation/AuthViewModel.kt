package com.smylo.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.core.network.error.AuthErrorParser
import com.smylo.core.network.error.NetworkErrorHandler
import com.smylo.feature.auth.data.AuthRepository
import com.smylo.feature.plan.data.PlanRepository
import com.smylo.feature.profile.data.UserRepository
import com.smylo.feature.timer.data.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthFlow(val purpose: String, val screen: String) {
    LOGIN(purpose = "login", screen = "auth_login"),
    REGISTER(purpose = "register", screen = "auth_register")
}

data class AuthUiState(
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val otpCode: String = "",
    val otpRequested: Boolean = false,
    val resendCooldownSeconds: Int = 0,
    val loading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val loggedIn: Boolean = false
)

private const val RESEND_COOLDOWN_SECONDS = 60

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val planRepository: PlanRepository,
    private val timerRepository: TimerRepository,
    private val userRepository: UserRepository,
    private val networkErrorHandler: NetworkErrorHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private var resendCooldownJob: Job? = null

    init {
        viewModelScope.launch {
            repository.observeLoggedIn().collectLatest { isLoggedIn ->
                _uiState.update { it.copy(loggedIn = isLoggedIn) }
            }
        }
    }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPhoneNumberChange(value: String) = _uiState.update { it.copy(phoneNumber = value) }
    fun onFirstNameChange(value: String) = _uiState.update { it.copy(firstName = value) }
    fun onLastNameChange(value: String) = _uiState.update { it.copy(lastName = value) }
    fun onDateOfBirthChange(value: String) = _uiState.update { it.copy(dateOfBirth = value) }
    fun onOtpChange(value: String) = _uiState.update { it.copy(otpCode = value) }

    fun dismissError() = _uiState.update { it.copy(error = null) }
    fun dismissSuccessMessage() = _uiState.update { it.copy(successMessage = null) }

    fun requestOtp(flow: AuthFlow) = sendOtp(flow = flow, isResend = false)

    fun resendOtp(flow: AuthFlow) = sendOtp(flow = flow, isResend = true)

    private fun sendOtp(flow: AuthFlow, isResend: Boolean) {
        val current = _uiState.value
        if (current.loading) return
        if (isResend && (!current.otpRequested || current.resendCooldownSeconds > 0)) return

        val endpoint = if (flow == AuthFlow.LOGIN) "/auth/login" else "/auth/register"
        _uiState.update { it.copy(loading = true, error = null, successMessage = null) }
        viewModelScope.launch {
            try {
                when (flow) {
                    AuthFlow.LOGIN -> repository.login(current.email, current.phoneNumber)
                    AuthFlow.REGISTER -> repository.register(current.email, current.phoneNumber)
                }
                _uiState.update {
                    it.copy(
                        loading = false,
                        otpRequested = true,
                        successMessage = "OTP sent successfully"
                    )
                }
                startResendCooldown()
            } catch (e: Exception) {
                val message = reportAuthError(e, flow.screen, endpoint)
                _uiState.update { it.copy(loading = false, error = message) }
            }
        }
    }

    fun verifyOtp(flow: AuthFlow) {
        if (_uiState.value.loading || _uiState.value.loggedIn) return
        _uiState.update { it.copy(loading = true, error = null, successMessage = null) }
        viewModelScope.launch {
            try {
                val state = _uiState.value
                repository.verifyOtp(
                    email = state.email,
                    phone = state.phoneNumber,
                    code = state.otpCode,
                    purpose = flow.purpose
                )

                if (flow == AuthFlow.REGISTER) {
                    runCatching {
                        userRepository.updateProfile(
                            firstName = state.firstName.takeIf { it.isNotBlank() },
                            lastName = state.lastName.takeIf { it.isNotBlank() },
                            dateOfBirth = state.dateOfBirth.takeIf { it.isNotBlank() }
                        )
                    }
                }

                runCatching {
                    userRepository.syncProfile()
                    planRepository.syncActivePlan()
                    if (planRepository.observePlan().first() != null) {
                        timerRepository.refreshSummary(force = true)
                    }
                }

                _uiState.update { it.copy(loading = false, loggedIn = true) }
            } catch (e: Exception) {
                val message = reportAuthError(e, flow.screen, "/auth/verify-otp")
                _uiState.update { it.copy(loading = false, error = message) }
            }
        }
    }

    private suspend fun reportAuthError(
        throwable: Throwable,
        screen: String,
        endpoint: String
    ): String {
        val authMessage = AuthErrorParser.parseUserMessage(throwable)
        return networkErrorHandler.report(
            throwable = throwable,
            screen = screen,
            endpoint = endpoint,
            overrideUserMessage = authMessage
        )
    }

    private fun startResendCooldown() {
        resendCooldownJob?.cancel()
        _uiState.update { it.copy(resendCooldownSeconds = RESEND_COOLDOWN_SECONDS) }
        resendCooldownJob = viewModelScope.launch {
            while (_uiState.value.resendCooldownSeconds > 0) {
                delay(1_000)
                _uiState.update {
                    val next = (it.resendCooldownSeconds - 1).coerceAtLeast(0)
                    it.copy(resendCooldownSeconds = next)
                }
            }
        }
    }

    override fun onCleared() {
        resendCooldownJob?.cancel()
        super.onCleared()
    }
}
