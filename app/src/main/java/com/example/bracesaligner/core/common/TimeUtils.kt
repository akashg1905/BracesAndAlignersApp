package com.example.bracesaligner.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object TimeUtils {
    fun nowMillis(): Long = System.currentTimeMillis()

    fun epochDayFromMillis(epochMillis: Long): Long {
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()
    }

    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()
}
