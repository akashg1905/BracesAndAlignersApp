package com.smylo.feature.profile.data

import com.smylo.core.network.api.UserApi
import com.smylo.core.network.dto.UpdateProfileRequest
import com.smylo.core.network.dto.UpdateUserSettingsRequest
import com.smylo.core.network.dto.UserProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    private val _profileFlow = MutableStateFlow<UserProfileResponse?>(null)
    val profileFlow: StateFlow<UserProfileResponse?> = _profileFlow.asStateFlow()

    private var cachedProfile: UserProfileResponse? = null

    suspend fun getUserProfile(force: Boolean = false): UserProfileResponse {
        if (force || cachedProfile == null) {
            cachedProfile = userApi.getUserProfile()
            _profileFlow.value = cachedProfile
        }
        return cachedProfile!!
    }

    suspend fun syncProfile() {
        getUserProfile(force = true)
    }

    suspend fun updateProfile(firstName: String?, lastName: String?, dateOfBirth: String?): UserProfileResponse {
        val response = userApi.updateProfile(
            UpdateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth
            )
        )
        cachedProfile = response
        _profileFlow.value = response
        return response
    }

    suspend fun updateProfileImage(image: MultipartBody.Part): UserProfileResponse {
        val response = userApi.updateProfileImage(image)
        cachedProfile = response
        _profileFlow.value = response
        return response
    }

    fun clearCache() {
        cachedProfile = null
        _profileFlow.value = null
    }

    suspend fun getSettingsCatalog() = userApi.getSettingsCatalog()

    suspend fun getUserSettings() = userApi.getUserSettings()

    suspend fun updateUserSettings(settings: Map<String, String>) =
        userApi.updateUserSettings(
            UpdateUserSettingsRequest(
                settings = settings.map { (name, value) ->
                    com.smylo.core.network.dto.UserSettingEntry(
                        settingName = name,
                        settingValue = value
                    )
                }
            )
        )
}

