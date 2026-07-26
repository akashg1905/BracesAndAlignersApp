package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class SettingsCatalogResponse(
    @SerializedName("settings")
    val settings: List<SettingCatalogItem>? = emptyList()
)

data class SettingCatalogItem(
    @SerializedName("settingName")
    val settingName: String? = null,
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("valueKind")
    val valueKind: String? = null
)

data class UserSettingsResponse(
    @SerializedName("settings")
    val settings: List<UserSettingResponseItem>? = null
)

data class UserSettingResponseItem(
    @SerializedName("settingId")
    val settingId: String? = null,
    @SerializedName("settingName")
    val settingName: String? = null,
    @SerializedName("settingValue")
    val settingValue: String? = null
)

data class UpdateUserSettingsRequest(
    @SerializedName("settings")
    val settings: List<UserSettingEntry>
)

data class UserSettingEntry(
    @SerializedName("settingName")
    val settingName: String,
    @SerializedName("settingValue")
    val settingValue: String
)
