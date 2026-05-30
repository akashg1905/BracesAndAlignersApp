package com.example.bracesaligner.core.network.dto

import com.google.gson.annotations.SerializedName

data class TimerSessionRequest(
    @SerializedName("sessionId")
    val sessionId: String,
    @SerializedName("planId")
    val planId: String? = null,
    @SerializedName("alignerNumber")
    val alignerNumber: Int,
    @SerializedName("startEpochMillis")
    val startEpochMillis: Long,
    @SerializedName("endEpochMillis")
    val endEpochMillis: Long
)

data class DailySummaryRequest(
    val dateEpochDay: Long,
    val totalMinutes: Int
)

data class DailySummaryResponse(
    val planId: String?,
    val totalNonWearHours: Double?,
    val averageDailyWearHours: Double?,
    val averageDailyWearDisplay: String?,
    val averageWearHours: Double?,
    val byAligner: List<AlignerBreakdownItem>?,
    val dailyBreakdown: List<DailyBreakdownItem>?,
    
    // Restoration fields
    @SerializedName("todayNonWearMinutes")
    val todayNonWearMinutes: Int? = null,
    @SerializedName("activeSession")
    val activeSession: TimerSessionResponse? = null
)

data class AlignerBreakdownItem(
    val alignerNumber: Int,
    val nonWearHours: Double,
    val sessionCount: Int
)

data class DailyBreakdownItem(
    val calendarDate: String,
    val nonWearHours: Double,
    val wearHours: Double,
    @SerializedName("sessionInformation")
    val sessions: List<TimerSessionResponse>? = null
)

data class TimerSessionResponse(
    @SerializedName(value = "sessionId", alternate = ["id", "session_id"])
    val sessionId: String,
    @SerializedName(value = "alignerNumber", alternate = ["aligner_number"])
    val alignerNumber: Int,
    @SerializedName(value = "startEpochMillis", alternate = ["start_epoch_millis"])
    val startEpochMillis: Long,
    @SerializedName(value = "endEpochMillis", alternate = ["end_epoch_millis"])
    val endEpochMillis: Long?
)
