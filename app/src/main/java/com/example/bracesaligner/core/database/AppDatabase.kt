package com.example.bracesaligner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bracesaligner.core.database.dao.AlignerPlanDao
import com.example.bracesaligner.core.database.dao.AuthSessionDao
import com.example.bracesaligner.core.database.dao.NonWearTimerDao
import com.example.bracesaligner.core.database.entity.AlignerPlanEntity
import com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity
import com.example.bracesaligner.core.database.entity.AuthSessionEntity
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity

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
