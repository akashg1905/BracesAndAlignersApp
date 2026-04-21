package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class OtpSendRequest(
    val email: String,
    @SerializedName("phone")
    val phoneNumber: String
)

data class OtpVerifyRequest(
    val email: String,
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("code")
    val otpCode: String
)

data class AuthTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    /** FastAPI often omits this; app falls back to JWT "sub" in [com.example.bracesaligner.feature.auth.data.AuthRepository]. */
    @SerializedName("userId")
    val userId: String? = null
)
