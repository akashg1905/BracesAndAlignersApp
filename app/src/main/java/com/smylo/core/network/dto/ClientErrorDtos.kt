package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class ClientErrorReportDto(
    @SerializedName("screen")
    val screen: String,
    @SerializedName("endpoint")
    val endpoint: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("userMessage")
    val userMessage: String,
    @SerializedName("technicalDetail")
    val technicalDetail: String,
    @SerializedName("httpStatus")
    val httpStatus: Int?,
    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("occurredAtEpochMillis")
    val occurredAtEpochMillis: Long
)

data class ClientErrorsBatchRequest(
    @SerializedName("errors")
    val errors: List<ClientErrorReportDto>
)
