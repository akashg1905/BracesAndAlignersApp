package com.smylo.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object TimeUtils {
    fun nowMillis(): Long = System.currentTimeMillis()

    fun epochDayFromMillis(epochMillis: Long): Long {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .toEpochDay()
    }

    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()

    fun getGreeting(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 0..11 -> "Morning"
            in 12..16 -> "Afternoon"
            else -> "Evening"
        }
    }

    /** Minimum closed non-wear session length that counts as a logged day for streak. */
    const val STREAK_MIN_SESSION_MILLIS = 2 * 60 * 1000L

    /**
     * Consecutive calendar days ending at [fromDay] where the user logged at least one
     * qualifying non-wear session. Defaults to today.
     * Days before [earliestDay] are ignored when set.
     */
    fun calculateStreakDays(
        qualifyingDays: Set<Long>,
        fromDay: Long = todayEpochDay(),
        earliestDay: Long? = null
    ): Int {
        var streak = 0
        var day = fromDay

        // If today hasn't qualified yet, check if there's a streak ending yesterday
        if (day !in qualifyingDays && day == todayEpochDay()) {
            day--
        }

        while (day in qualifyingDays && (earliestDay == null || day >= earliestDay)) {
            streak++
            day--
        }
        return streak
    }

    fun formatDurationHMS(millis: Long): String {
        val totalSeconds = millis / 1000
        val h = totalSeconds / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        
        return buildString {
            if (h > 0) append("${h}h ")
            if (m > 0 || h > 0) append("${m}m ")
            append("${s}s")
        }.trim()
    }
}

