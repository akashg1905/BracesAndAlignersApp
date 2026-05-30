package com.example.bracesaligner.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.feature.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val email: String = "",
    val phone: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val profile = userRepository.getUserProfile()
                _uiState.value = _uiState.value.copy(
                    email = profile.email,
                    phone = profile.phone,
                    firstName = profile.firstName ?: "",
                    lastName = profile.lastName ?: "",
                    dob = profile.dateOfBirth ?: "",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load profile: ${e.message}"
                )
            }
        }
    }

    fun updateFirstName(newValue: String) {
        _uiState.value = _uiState.value.copy(firstName = newValue)
        syncProfile()
    }

    fun updateLastName(newValue: String) {
        _uiState.value = _uiState.value.copy(lastName = newValue)
        syncProfile()
    }

    fun updateDob(newValue: String) {
        _uiState.value = _uiState.value.copy(dob = newValue)
        syncProfile()
    }

    private fun syncProfile() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                userRepository.updateProfile(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    dateOfBirth = state.dob
                )
            } catch (e: Exception) {
                // Handle error (maybe show a toast or revert state)
            }
        }
    }
}
