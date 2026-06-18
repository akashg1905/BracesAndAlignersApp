package com.example.bracesaligner.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bracesaligner.feature.profile.data.UserRepository
import com.example.bracesaligner.feature.plan.data.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val email: String = "",
    val phone: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val profileImage: String? = null,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val hasChanges: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null,
    val planAvailable: Boolean = false,
    val isPlanExpired: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var initialProfile: ProfileUiState? = null

    init {
        viewModelScope.launch {
            planRepository.observePlan().collect { plan ->
                _uiState.value = _uiState.value.copy(
                    planAvailable = plan != null,
                    isPlanExpired = plan?.isExpired ?: false
                )
            }
        }
    }

    fun loadProfile() {
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
                    profileImage = profile.profileImage,
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

    fun updateProfileImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, error = null)
            try {
                val profile = userRepository.updateProfileImage(image)
                _uiState.value = _uiState.value.copy(
                    profileImage = profile.profileImage,
                    isUpdating = false,
                    successMessage = "Profile image updated!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    error = "Failed to update image: ${e.message}"
                )
            }
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
            _uiState.value = _uiState.value.copy(isUpdating = true, error = null, successMessage = null)
            try {
                val state = _uiState.value
                userRepository.updateProfile(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    dateOfBirth = state.dob
                )
                
                val updatedState = state.copy(
                    hasChanges = false, 
                    isUpdating = false,
                    successMessage = "Profile updated successfully!"
                )
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

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
