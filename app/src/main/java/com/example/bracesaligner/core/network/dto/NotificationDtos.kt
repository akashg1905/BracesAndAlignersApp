package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequest(
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("platform")
    val platform: String = "android",
    @SerializedName("device_model")
    val deviceModel: String,
    @SerializedName("app_version")
    val appVersion: String
)

data class NotificationDispatchRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("non_wear_time")
    val nonWearTime: Int? = null
)

data class NotificationDispatchResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("msg")
    val msg: String? = null,
    @SerializedName("body")
    val body: String? = null,
    @SerializedName("title")
    val title: String? = null
) {
    fun getDisplayMessage(): String? = message ?: msg ?: body
}
