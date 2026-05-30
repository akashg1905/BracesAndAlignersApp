package com.example.bracesaligner.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bracesaligner.core.database.AppDatabase
import com.example.bracesaligner.core.database.dao.AlignerPlanDao
import com.example.bracesaligner.core.database.dao.AuthSessionDao
import com.example.bracesaligner.core.database.dao.NonWearTimerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE aligner_plan ADD COLUMN planStatus TEXT DEFAULT NULL")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            try {
                // First check if column exists to avoid crash
                val cursor = db.query("PRAGMA table_info(non_wear_session)")
                var columnExists = false
                val nameIndex = cursor.getColumnIndex("name")
                if (nameIndex != -1) {
                    while (cursor.moveToNext()) {
                        if (cursor.getString(nameIndex) == "lastNotificationMinutes") {
                            columnExists = true
                            break
                        }
                    }
                }
                cursor.close()

                if (!columnExists) {
                    db.execSQL("ALTER TABLE non_wear_session ADD COLUMN lastNotificationMinutes INTEGER NOT NULL DEFAULT 0")
                }
            } catch (e: Exception) {
                android.util.Log.e("DatabaseModule", "Migration 3->4 failed or column already exists", e)
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "braces_db")
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .fallbackToDestructiveMigration() // Safety net for development
            .build()
    }

    @Provides
    fun provideAuthSessionDao(db: AppDatabase): AuthSessionDao = db.authSessionDao()

    @Provides
    fun provideAlignerPlanDao(db: AppDatabase): AlignerPlanDao = db.alignerPlanDao()

    @Provides
    fun provideNonWearTimerDao(db: AppDatabase): NonWearTimerDao = db.nonWearTimerDao()
}
