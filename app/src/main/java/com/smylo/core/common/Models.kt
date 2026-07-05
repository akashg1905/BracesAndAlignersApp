package com.smylo.core.common

data class AuthSession(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String
)

data class AlignerPlan(
    val planId: String,
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long,
    val planStatus: String? = null
) {
    val isExpired: Boolean
        get() = planStatus.equals("expired", ignoreCase = true)
}

data class AlignerScheduleItem(
    val id: String = "",
    val alignerNumber: Int,
    val daysForAligner: Int = 0,
    val startEpochDay: Long,
    val endEpochDay: Long,
    val isCurrent: Boolean = false,
    val startDate: String = "",
    val endDate: String = ""
)

data class TimerState(
    val isRunning: Boolean = false,
    val activeSessionStart: Long? = null,
    val todayTotalMillis: Long = 0L,
    val warningMinutes: Int = 90,
    val limitMinutes: Int = 120
)

