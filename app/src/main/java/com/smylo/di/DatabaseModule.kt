package com.smylo.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smylo.core.database.AppDatabase
import com.smylo.core.database.dao.AlignerPlanDao
import com.smylo.core.database.dao.AuthSessionDao
import com.smylo.core.database.dao.ClientErrorDao
import com.smylo.core.database.dao.NonWearTimerDao
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

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS client_error_log (
                    id TEXT NOT NULL PRIMARY KEY,
                    screen TEXT NOT NULL,
                    endpoint TEXT,
                    category TEXT NOT NULL,
                    userMessage TEXT NOT NULL,
                    technicalDetail TEXT NOT NULL,
                    httpStatus INTEGER,
                    appVersion TEXT NOT NULL,
                    occurredAtMillis INTEGER NOT NULL,
                    synced INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "braces_db")
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_5_6)
            .fallbackToDestructiveMigration() // Safety net for development
            .build()
    }

    @Provides
    fun provideAuthSessionDao(db: AppDatabase): AuthSessionDao = db.authSessionDao()

    @Provides
    fun provideAlignerPlanDao(db: AppDatabase): AlignerPlanDao = db.alignerPlanDao()

    @Provides
    fun provideNonWearTimerDao(db: AppDatabase): NonWearTimerDao = db.nonWearTimerDao()

    @Provides
    fun provideClientErrorDao(db: AppDatabase): ClientErrorDao = db.clientErrorDao()
}

