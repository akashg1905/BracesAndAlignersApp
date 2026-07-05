package com.smylo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smylo.core.database.dao.AlignerPlanDao
import com.smylo.core.database.dao.AuthSessionDao
import com.smylo.core.database.dao.NonWearTimerDao
import com.smylo.core.database.entity.AlignerPlanEntity
import com.smylo.core.database.entity.AlignerScheduleItemEntity
import com.smylo.core.database.entity.AuthSessionEntity
import com.smylo.core.database.entity.DailyNonWearSummaryEntity
import com.smylo.core.database.entity.NonWearSessionEntity

@Database(
    entities = [
        AuthSessionEntity::class,
        AlignerPlanEntity::class,
        AlignerScheduleItemEntity::class,
        NonWearSessionEntity::class,
        DailyNonWearSummaryEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authSessionDao(): AuthSessionDao
    abstract fun alignerPlanDao(): AlignerPlanDao
    abstract fun nonWearTimerDao(): NonWearTimerDao
}

