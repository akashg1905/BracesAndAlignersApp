package com.example.bracesaligner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_session")
data class AuthSessionEntity(
    @PrimaryKey val id: Int = 1,
    val accessToken: String,
    val refreshToken: String?,
    val userId: String,
    val isLoggedIn: Boolean = true
)
