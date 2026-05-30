package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String? // Assuming ISO format "YYYY-MM-DD"
)

data class UpdateProfileRequest(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?
)
