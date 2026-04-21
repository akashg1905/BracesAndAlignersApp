package com.example.bracesaligner.core.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/example/bracesaligner/core/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "alignerPlanDao", "Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;", "authSessionDao", "Lcom/example/bracesaligner/core/database/dao/AuthSessionDao;", "nonWearTimerDao", "Lcom/example/bracesaligner/core/database/dao/NonWearTimerDao;", "app_debug"})
@androidx.room.Database(entities = {com.example.bracesaligner.core.database.entity.AuthSessionEntity.class, com.example.bracesaligner.core.database.entity.AlignerPlanEntity.class, com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity.class, com.example.bracesaligner.core.database.entity.NonWearSessionEntity.class, com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.bracesaligner.core.database.dao.AuthSessionDao authSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.bracesaligner.core.database.dao.AlignerPlanDao alignerPlanDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.bracesaligner.core.database.dao.NonWearTimerDao nonWearTimerDao();
}