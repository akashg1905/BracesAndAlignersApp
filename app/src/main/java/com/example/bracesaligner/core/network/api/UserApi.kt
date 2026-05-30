package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.UpdateProfileRequest
import com.example.bracesaligner.core.network.dto.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("/api/users/me")
    suspend fun getUserProfile(): UserProfileResponse

    @PATCH("/api/users/me")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): UserProfileResponse
}
