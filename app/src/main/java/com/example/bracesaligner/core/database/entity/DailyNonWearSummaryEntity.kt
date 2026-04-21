package com.example.bracesaligner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_non_wear_summary")
data class DailyNonWearSummaryEntity(
    @PrimaryKey val dateEpochDay: Long,
    val totalMinutes: Int,
    val warningSent: Boolean = false,
    val exceededSent: Boolean = false
)
