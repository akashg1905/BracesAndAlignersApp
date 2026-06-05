package com.example.bracesaligner.feature.profile.data

import com.example.bracesaligner.core.network.api.UserApi
import com.example.bracesaligner.core.network.dto.UpdateProfileRequest
import com.example.bracesaligner.core.network.dto.UserProfileResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    private var cachedProfile: UserProfileResponse? = null

    suspend fun getUserProfile(force: Boolean = false): UserProfileResponse {
        if (force || cachedProfile == null) {
            cachedProfile = userApi.getUserProfile()
        }
        return cachedProfile!!
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
        return response
    }

    suspend fun updateProfileImage(image: MultipartBody.Part): UserProfileResponse {
        val response = userApi.updateProfileImage(image)
        cachedProfile = response
        return response
    }

    fun clearCache() {
        cachedProfile = null
    }
}
