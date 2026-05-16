package com.example.bracesaligner.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.bracesaligner.core.database.entity.AlignerPlanEntity
import com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlignerPlanDao {
    @Query("SELECT * FROM aligner_plan ORDER BY createdAtEpochMillis DESC LIMIT 1")
    fun observePlan(): Flow<AlignerPlanEntity?>

    @Query("SELECT * FROM aligner_plan ORDER BY createdAtEpochMillis DESC LIMIT 1")
    suspend fun getPlan(): AlignerPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlan(plan: AlignerPlanEntity)

    @Query("DELETE FROM aligner_schedule WHERE planId = :planId")
    suspend fun clearSchedule(planId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(items: List<AlignerScheduleItemEntity>)

    @Query("SELECT * FROM aligner_schedule WHERE planId = :planId ORDER BY alignerNumber ASC")
    fun observeSchedule(planId: String): Flow<List<AlignerScheduleItemEntity>>

    @Query("DELETE FROM aligner_plan")
    suspend fun deleteAllPlans()

    @Query("DELETE FROM aligner_schedule")
    suspend fun deleteAllSchedules()

    @Transaction
    suspend fun clearAllPlansAndSchedules() {
        deleteAllSchedules()
        deleteAllPlans()
    }
}
