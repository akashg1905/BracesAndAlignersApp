package com.example.bracesaligner.core.network.dto

data class CreatePlanRequest(
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long
)

data class AlignerPlanResponse(
    val planId: String,
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long
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
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val startDate: String,
    val endDate: String,
    val isCurrent: Boolean
)
