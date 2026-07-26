package com.smylo.core.network.dto

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
    @SerializedName("dateEpochDay")
    val dateEpochDay: Long,
    @SerializedName("totalMinutes")
    val totalMinutes: Int
)

data class DailySummaryResponse(
    @SerializedName("planId")
    val planId: String?,
    @SerializedName("totalNonWearHours")
    val totalNonWearHours: Double?,
    @SerializedName("averageDailyWearHours")
    val averageDailyWearHours: Double?,
    @SerializedName("averageDailyWearDisplay")
    val averageDailyWearDisplay: String?,
    @SerializedName("averageWearHours")
    val averageWearHours: Double?,
    @SerializedName("byAligner")
    val byAligner: List<AlignerBreakdownItem>?,
    @SerializedName("dailyBreakdown")
    val dailyBreakdown: List<DailyBreakdownItem>?,
    @SerializedName("todayNonWearMinutes")
    val todayNonWearMinutes: Int? = null,
    @SerializedName("activeSession")
    val activeSession: TimerSessionResponse? = null
)

data class AlignerBreakdownItem(
    @SerializedName("alignerNumber")
    val alignerNumber: Int,
    @SerializedName("nonWearHours")
    val nonWearHours: Double,
    @SerializedName("sessionCount")
    val sessionCount: Int
)

data class DailyBreakdownItem(
    @SerializedName("calendarDate")
    val calendarDate: String,
    @SerializedName("nonWearHours")
    val nonWearHours: Double,
    @SerializedName("wearHours")
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

