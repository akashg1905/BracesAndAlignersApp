package com.smylo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smylo.core.database.entity.AuthSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthSessionDao {
    @Query("SELECT * FROM auth_session WHERE id = 1 LIMIT 1")
    fun observeSession(): Flow<AuthSessionEntity?>

    @Query("SELECT * FROM auth_session WHERE id = 1 LIMIT 1")
    suspend fun getSession(): AuthSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: AuthSessionEntity)

    @Query("DELETE FROM auth_session")
    suspend fun clearSession()
}

