package com.smylo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smylo.core.database.entity.ClientErrorEntity

@Dao
interface ClientErrorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(error: ClientErrorEntity)

    @Query("SELECT * FROM client_error_log WHERE synced = 0 ORDER BY occurredAtMillis ASC LIMIT :limit")
    suspend fun getUnsynced(limit: Int = 50): List<ClientErrorEntity>

    @Query("UPDATE client_error_log SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<String>)

    @Query("DELETE FROM client_error_log WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT COUNT(*) FROM client_error_log WHERE synced = 0")
    suspend fun countUnsynced(): Int
}
