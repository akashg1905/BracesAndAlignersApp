package com.smylo.core.network.dto

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
    val dateOfBirth: String?,
    @SerializedName("profileImage")
    val profileImage: String? = null
)

data class UpdateProfileRequest(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,
    @SerializedName("profileImage")
    val profileImage: String? = null
)

