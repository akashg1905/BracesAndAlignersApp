package com.smylo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "non_wear_session")
data class NonWearSessionEntity(
    @PrimaryKey val sessionId: String,
    val alignerNumber: Int,
    val startEpochMillis: Long,
    val endEpochMillis: Long?,
    val dateEpochDay: Long,
    val synced: Boolean = false,
    val lastNotificationMinutes: Int = 0
)

