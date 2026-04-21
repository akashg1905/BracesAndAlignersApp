package com.example.bracesaligner.core.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u00032\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0010H\'J\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u00102\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;", "", "clearSchedule", "", "planId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPlan", "Lcom/example/bracesaligner/core/database/entity/AlignerPlanEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSchedule", "items", "", "Lcom/example/bracesaligner/core/database/entity/AlignerScheduleItemEntity;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observePlan", "Lkotlinx/coroutines/flow/Flow;", "observeSchedule", "upsertPlan", "plan", "(Lcom/example/bracesaligner/core/database/entity/AlignerPlanEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AlignerPlanDao {
    
    @androidx.room.Query(value = "SELECT * FROM aligner_plan LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.bracesaligner.core.database.entity.AlignerPlanEntity> observePlan();
    
    @androidx.room.Query(value = "SELECT * FROM aligner_plan LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPlan(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.database.entity.AlignerPlanEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertPlan(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.entity.AlignerPlanEntity plan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM aligner_schedule WHERE planId = :planId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearSchedule(@org.jetbrains.annotations.NotNull()
    java.lang.String planId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSchedule(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM aligner_schedule WHERE planId = :planId ORDER BY alignerNumber ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity>> observeSchedule(@org.jetbrains.annotations.NotNull()
    java.lang.String planId);
}