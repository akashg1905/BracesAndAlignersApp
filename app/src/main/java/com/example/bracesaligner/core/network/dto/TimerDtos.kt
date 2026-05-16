package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class TimerSessionRequest(
    val sessionId: String,
    val alignerNumber: Int,
    val startEpochMillis: Long,
    val endEpochMillis: Long?
)

data class DailySummaryRequest(
    val dateEpochDay: Long,
    val totalMinutes: Int
)

data class DailySummaryResponse(
    @SerializedName("averageDailyWearHours")
    val averageDailyWearHours: Double?,
    @SerializedName("averageDailyWearDisplay")
    val averageDailyWearDisplay: String?
)
