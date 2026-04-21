package com.example.bracesaligner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aligner_schedule")
data class AlignerScheduleItemEntity(
    @PrimaryKey val id: String,
    val planId: String,
    val alignerNumber: Int,
    val startEpochDay: Long,
    val endEpochDay: Long
)
