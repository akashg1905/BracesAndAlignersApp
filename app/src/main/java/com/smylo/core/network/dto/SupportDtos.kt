package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class SupportTopic(
    @SerializedName("topic")
    val topic: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String
)

data class SupportTopicsResponse(
    @SerializedName("topics")
    val topics: List<SupportTopic>
)

data class CreateSupportQueryRequest(
    @SerializedName("topic")
    val topic: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("appVersion")
    val appVersion: String
)

data class CreateSupportQueryResponse(
    @SerializedName("ticketId")
    val ticketId: String,
    @SerializedName("detail")
    val detail: String
)
