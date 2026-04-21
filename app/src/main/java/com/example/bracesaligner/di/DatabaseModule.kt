package com.example.bracesaligner.di

import android.content.Context
import androidx.room.Room
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
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "braces_db").build()
    }

    @Provides
    fun provideAuthSessionDao(db: AppDatabase): AuthSessionDao = db.authSessionDao()

    @Provides
    fun provideAlignerPlanDao(db: AppDatabase): AlignerPlanDao = db.alignerPlanDao()

    @Provides
    fun provideNonWearTimerDao(db: AppDatabase): NonWearTimerDao = db.nonWearTimerDao()
}
