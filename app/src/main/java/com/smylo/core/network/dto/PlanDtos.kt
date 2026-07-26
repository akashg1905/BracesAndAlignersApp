package com.smylo.core.network.dto

import com.google.gson.annotations.SerializedName

data class CreatePlanRequest(
    @SerializedName("alignerCount")
    val alignerCount: Int,
    @SerializedName("daysPerAligner")
    val daysPerAligner: Int,
    @SerializedName("startDateEpochDay")
    val startDateEpochDay: Long
)

data class AlignerPlanResponse(
    @SerializedName("planId")
    val planId: String,
    @SerializedName("alignerCount")
    val alignerCount: Int,
    @SerializedName("daysPerAligner")
    val daysPerAligner: Int,
    @SerializedName("startDateEpochDay")
    val startDateEpochDay: Long,
    /** `"expired"` when plan exists but today is past the last day; omit or other value when active. */
    @SerializedName(value = "plan_status", alternate = ["planStatus"])
    val planStatus: String? = null
)

data class AlignerScheduleResponse(
    @SerializedName("planId")
    val planId: String,
    @SerializedName("todayEpochDay")
    val todayEpochDay: Long,
    @SerializedName("currentAlignerNumber")
    val currentAlignerNumber: Int,
    @SerializedName("schedule")
    val schedule: List<AlignerScheduleItemDto>
)

data class AlignerScheduleItemDto(
    @SerializedName("alignerId")
    val alignerId: String,
    @SerializedName("alignerNumber")
    val alignerNumber: Int,
    @SerializedName("daysForAligner")
    val daysForAligner: Int = 0,
    @SerializedName("startDateEpochDay")
    val startDateEpochDay: Long,
    @SerializedName("endDateEpochDay")
    val endDateEpochDay: Long,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("isCurrent")
    val isCurrent: Boolean
)

data class UpdateAlignerRequest(
    @SerializedName("alignerId")
    val alignerId: String,
    @SerializedName("daysForAligner")
    val daysForAligner: Int
)

data class UpdatePlanScheduleRequest(
    @SerializedName("planId")
    val planId: String? = null,
    @SerializedName("updates")
    val updates: List<UpdateAlignerRequest>
)

