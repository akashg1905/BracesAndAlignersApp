package com.example.bracesaligner.core.common

import java.util.Calendar
import java.util.TimeZone

object TimeUtils {
    fun nowMillis(): Long = System.currentTimeMillis()

    fun epochDayFromMillis(epochMillis: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = epochMillis
        // Clear time components to get just the date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis / (24 * 60 * 60 * 1000L)
    }

    fun todayEpochDay(): Long = epochDayFromMillis(nowMillis())

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Morning"
            in 12..16 -> "Afternoon"
            else -> "Evening"
        }
    }
}
