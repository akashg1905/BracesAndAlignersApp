package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("mobileNumber")
    val mobileNumber: String,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?
)

data class UpdateProfileRequest(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?
)
