package com.smylo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aligner_plan")
data class AlignerPlanEntity(
    @PrimaryKey val planId: String,
    val userId: String,
    val alignerCount: Int,
    val daysPerAligner: Int,
    val startDateEpochDay: Long,
    val createdAtEpochMillis: Long,
    /** Local copy of server status, e.g. `expired`. */
    val planStatus: String? = null
)

