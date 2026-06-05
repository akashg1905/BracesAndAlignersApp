package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class CreatePlanRequest(
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long
)

data class AlignerPlanResponse(
    val planId: String,
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long,
    /** `"expired"` when plan exists but today is past the last day; omit or other value when active. */
    @SerializedName(value = "plan_status", alternate = ["planStatus"])
    val planStatus: String? = null
)

data class AlignerScheduleResponse(
    val planId: String,
    val todayEpochDay: Long,
    val currentAlignerNumber: Int,
    val schedule: List<AlignerScheduleItemDto>
)

data class AlignerScheduleItemDto(
    val alignerId: String,
    val alignerNumber: Int,
    val daysForAligner: Int = 0,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val startDate: String,
    val endDate: String,
    val isCurrent: Boolean
)

data class UpdateAlignerRequest(
    val alignerId: String,
    val daysForAligner: Int
)

data class UpdatePlanScheduleRequest(
    val planId: String? = null,
    val updates: List<UpdateAlignerRequest>
)
