package com.smylo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smylo.core.database.entity.DailyNonWearSummaryEntity
import com.smylo.core.database.entity.NonWearSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NonWearTimerDao {
    @Query("SELECT * FROM non_wear_session WHERE (endEpochMillis IS NULL OR endEpochMillis <= 0) ORDER BY startEpochMillis DESC LIMIT 1")
    fun observeActiveSession(): Flow<NonWearSessionEntity?>

    @Query("SELECT * FROM non_wear_session WHERE (endEpochMillis IS NULL OR endEpochMillis <= 0) ORDER BY startEpochMillis DESC LIMIT 1")
    suspend fun getActiveSession(): NonWearSessionEntity?

    @Query("SELECT * FROM non_wear_session ORDER BY startEpochMillis DESC")
    suspend fun getAllSessions(): List<NonWearSessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: NonWearSessionEntity)

    @Query("UPDATE non_wear_session SET endEpochMillis = :endMillis WHERE sessionId = :sessionId")
    suspend fun stopSession(sessionId: String, endMillis: Long)

    @Query("SELECT * FROM non_wear_session WHERE dateEpochDay = :epochDay ORDER BY startEpochMillis ASC")
    fun observeSessionsForDay(epochDay: Long): Flow<List<NonWearSessionEntity>>

    @Query("SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = :epochDay AND endEpochMillis IS NOT NULL")
    fun observeDayTotalMillis(epochDay: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = :epochDay AND endEpochMillis IS NOT NULL")
    suspend fun getDayTotalMillis(epochDay: Long): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailySummary(summary: DailyNonWearSummaryEntity)

    @Query("SELECT * FROM daily_non_wear_summary WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getDailySummary(epochDay: Long): DailyNonWearSummaryEntity?

    @Query("SELECT * FROM daily_non_wear_summary ORDER BY dateEpochDay DESC LIMIT :limit")
    fun observeRecentSummary(limit: Int): Flow<List<DailyNonWearSummaryEntity>>

    @Query("SELECT * FROM non_wear_session WHERE synced = 0 AND endEpochMillis IS NOT NULL")
    suspend fun getUnsyncedSessions(): List<NonWearSessionEntity>

    @Query("UPDATE non_wear_session SET synced = 1 WHERE sessionId IN (:sessionIds)")
    suspend fun markAsSynced(sessionIds: List<String>)

    @Query("UPDATE non_wear_session SET lastNotificationMinutes = :minutes WHERE sessionId = :sessionId")
    suspend fun updateLastNotification(sessionId: String, minutes: Int)

    @Query("SELECT DISTINCT dateEpochDay FROM non_wear_session ORDER BY dateEpochDay DESC")
    fun observeAllAvailableDays(): Flow<List<Long>>

    @Query(
        """
        SELECT DISTINCT dateEpochDay FROM non_wear_session
        WHERE endEpochMillis IS NOT NULL
        AND endEpochMillis > startEpochMillis
        AND (endEpochMillis - startEpochMillis) >= :minDurationMillis
        ORDER BY dateEpochDay DESC
        """
    )
    fun observeQualifyingStreakDays(minDurationMillis: Long): Flow<List<Long>>
}

