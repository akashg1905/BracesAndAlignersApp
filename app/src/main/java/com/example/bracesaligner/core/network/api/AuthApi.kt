package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.AuthTokenResponse
import com.example.bracesaligner.core.network.dto.OtpSendRequest
import com.example.bracesaligner.core.network.dto.OtpVerifyRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/register")
    suspend fun sendOtp(@Body body: OtpSendRequest)

    @POST("/auth/verify-otp")
    suspend fun verifyOtp(@Body body: OtpVerifyRequest): AuthTokenResponse
}
