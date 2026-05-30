package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.AuthTokenResponse
import com.example.bracesaligner.core.network.dto.DeviceTokenRequest
import com.example.bracesaligner.core.network.dto.OtpSendRequest
import com.example.bracesaligner.core.network.dto.OtpVerifyRequest
import com.example.bracesaligner.core.network.dto.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/auth/register")
    suspend fun sendOtp(@Body body: OtpSendRequest)

    @POST("/auth/verify-otp")
    suspend fun verifyOtp(@Body body: OtpVerifyRequest): AuthTokenResponse

    @POST("/auth/refresh")
    suspend fun refresh(@Body body: RefreshTokenRequest): AuthTokenResponse

    @POST("/auth/device-token")
    suspend fun registerDevice(@Body body: DeviceTokenRequest)

    @DELETE("/auth/device-token")
    suspend fun unregisterDevice(@Query("fcmToken") fcmToken: String?)
}
