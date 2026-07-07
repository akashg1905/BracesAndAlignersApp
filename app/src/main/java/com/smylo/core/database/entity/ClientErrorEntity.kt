package com.smylo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_error_log")
data class ClientErrorEntity(
    @PrimaryKey val id: String,
    val screen: String,
    val endpoint: String?,
    val category: String,
    val userMessage: String,
    val technicalDetail: String,
    val httpStatus: Int?,
    val appVersion: String,
    val occurredAtMillis: Long,
    val synced: Boolean = false
)
