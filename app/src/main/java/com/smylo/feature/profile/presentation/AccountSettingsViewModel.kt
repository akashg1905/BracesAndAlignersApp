package com.smylo.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.core.preferences.SessionStore
import com.smylo.core.network.error.NetworkErrorHandler
import com.smylo.feature.auth.data.AuthRepository
import com.smylo.feature.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingItemUi(
    val settingName: String,
    val label: String,
    val description: String,
    val valueKind: String,
    val value: String,
    /** True when the backend has no saved value yet — shown with highlight styling. */
    val isUnset: Boolean
)

data class AccountSettingsUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val settings: List<SettingItemUi> = emptyList(),
    val successMessage: String? = null,
    val error: String? = null,
    val loggedOut: Boolean = false
)

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val sessionStore: SessionStore,
    private val networkErrorHandler: NetworkErrorHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountSettingsUiState())
    val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val catalog = userRepository.getSettingsCatalog()
                val saved = userRepository.getUserSettings()
                val savedMap = saved.settings.orEmpty()

                val items = catalog.settings.map { item ->
                    val savedValue = savedMap[item.settingName].orEmpty()
                    SettingItemUi(
                        settingName = item.settingName,
                        label = item.label,
                        description = item.description.orEmpty(),
                        valueKind = item.valueKind.lowercase(),
                        value = savedValue,
                        isUnset = savedValue.isBlank()
                    )
                }

                _uiState.update {
                    it.copy(isLoading = false, settings = items)
                }
            } catch (e: Exception) {
                val message = networkErrorHandler.report(
                    e,
                    screen = "account_settings",
                    endpoint = "/api/users/me/settings"
                )
                _uiState.update {
                    it.copy(isLoading = false, error = message)
                }
            }
        }
    }

    fun updateSettingValue(settingName: String, value: String) {
        _uiState.update { state ->
            state.copy(
                settings = state.settings.map { item ->
                    if (item.settingName == settingName) {
                        item.copy(value = value, isUnset = value.isBlank())
                    } else item
                }
            )
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }
            try {
                val payload = _uiState.value.settings
                    .filter { it.value.isNotBlank() }
                    .associate { it.settingName to it.value }

                userRepository.updateUserSettings(payload)

                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        successMessage = "Settings saved successfully",
                        settings = state.settings.map { item ->
                            item.copy(isUnset = item.value.isBlank())
                        }
                    )
                }
            } catch (e: Exception) {
                val message = networkErrorHandler.report(
                    e,
                    screen = "account_settings",
                    endpoint = "/api/users/me/settings"
                )
                _uiState.update {
                    it.copy(isSaving = false, error = message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val token = sessionStore.fcmToken.first()
                authRepository.logout(token)
                _uiState.update { it.copy(loggedOut = true) }
            } catch (e: Exception) {
                networkErrorHandler.report(e, screen = "account_settings", endpoint = "/auth/logout")
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
