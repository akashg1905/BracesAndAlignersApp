package com.example.bracesaligner.feature.profile.data

import com.example.bracesaligner.core.network.api.UserApi
import com.example.bracesaligner.core.network.dto.UpdateProfileRequest
import com.example.bracesaligner.core.network.dto.UserProfileResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getUserProfile(): UserProfileResponse {
        return userApi.getUserProfile()
    }

    suspend fun updateProfile(firstName: String?, lastName: String?, dateOfBirth: String?): UserProfileResponse {
        return userApi.updateProfile(
            UpdateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth
            )
        )
    }
}
