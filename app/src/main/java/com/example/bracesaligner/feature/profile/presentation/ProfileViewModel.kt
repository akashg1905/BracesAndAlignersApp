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
    val isUpdating: Boolean = false,
    val hasChanges: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var initialProfile: ProfileUiState? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val profile = userRepository.getUserProfile()
                val loadedState = ProfileUiState(
                    email = profile.email,
                    phone = profile.mobileNumber,
                    firstName = profile.firstName ?: "",
                    lastName = profile.lastName ?: "",
                    dob = profile.dateOfBirth ?: "",
                    isLoading = false
                )
                initialProfile = loadedState
                _uiState.value = loadedState
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load profile: ${e.message}"
                )
            }
        }
    }

    fun updateFirstName(newValue: String) {
        _uiState.value = _uiState.value.copy(firstName = newValue).let { 
            it.copy(hasChanges = checkChanges(it))
        }
    }

    fun updateLastName(newValue: String) {
        _uiState.value = _uiState.value.copy(lastName = newValue).let {
            it.copy(hasChanges = checkChanges(it))
        }
    }

    fun updateDob(newValue: String) {
        _uiState.value = _uiState.value.copy(dob = newValue).let {
            it.copy(hasChanges = checkChanges(it))
        }
    }

    private fun checkChanges(state: ProfileUiState): Boolean {
        val initial = initialProfile ?: return false
        return state.firstName != initial.firstName ||
               state.lastName != initial.lastName ||
               state.dob != initial.dob
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, error = null)
            try {
                val state = _uiState.value
                // Send values to backend exactly as they are in the input fields
                userRepository.updateProfile(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    dateOfBirth = state.dob
                )
                
                // After successful update, refresh initial state
                val updatedState = state.copy(hasChanges = false, isUpdating = false)
                initialProfile = updatedState
                _uiState.value = updatedState
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    error = "Update failed: ${e.localizedMessage}"
                )
            }
        }
    }
}
