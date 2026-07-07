package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class SettingsCatalogResponse(
    @SerializedName("settings")
    val settings: List<SettingCatalogItem> = emptyList()
)

data class SettingCatalogItem(
    @SerializedName("settingName")
    val settingName: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("valueKind")
    val valueKind: String
)

data class UserSettingsResponse(
    @SerializedName("settings")
    val settings: Map<String, String>? = null
)

data class UpdateUserSettingsRequest(
    @SerializedName("settings")
    val settings: Map<String, String>
)
