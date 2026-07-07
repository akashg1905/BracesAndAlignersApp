package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class AuthCredentialsRequest(
    val email: String,
    @SerializedName("phone")
    val phone: String
)

data class OtpVerifyRequest(
    @SerializedName("code")
    val code: String,
    val email: String,
    @SerializedName("phone")
    val phone: String,
    val purpose: String
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class AuthTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("userId")
    val userId: String? = null
)
