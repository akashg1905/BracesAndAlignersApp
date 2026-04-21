package com.example.bracesaligner.core.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\fH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0013H\'J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u00132\u0006\u0010\u0007\u001a\u00020\bH\'J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\f0\u00132\u0006\u0010\u0016\u001a\u00020\u0017H\'J\u001e\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010!\u00a8\u0006\""}, d2 = {"Lcom/example/bracesaligner/core/database/dao/NonWearTimerDao;", "", "getActiveSession", "Lcom/example/bracesaligner/core/database/entity/NonWearSessionEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDailySummary", "Lcom/example/bracesaligner/core/database/entity/DailyNonWearSummaryEntity;", "epochDay", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDayTotalMillis", "getUnsyncedSessions", "", "markAsSynced", "", "sessionIds", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeActiveSession", "Lkotlinx/coroutines/flow/Flow;", "observeDayTotalMillis", "observeRecentSummary", "limit", "", "stopSession", "sessionId", "endMillis", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertDailySummary", "summary", "(Lcom/example/bracesaligner/core/database/entity/DailyNonWearSummaryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertSession", "session", "(Lcom/example/bracesaligner/core/database/entity/NonWearSessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface NonWearTimerDao {
    
    @androidx.room.Query(value = "SELECT * FROM non_wear_session WHERE endEpochMillis IS NULL LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.bracesaligner.core.database.entity.NonWearSessionEntity> observeActiveSession();
    
    @androidx.room.Query(value = "SELECT * FROM non_wear_session WHERE endEpochMillis IS NULL LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.database.entity.NonWearSessionEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertSession(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.entity.NonWearSessionEntity session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE non_wear_session SET endEpochMillis = :endMillis WHERE sessionId = :sessionId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object stopSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, long endMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = :epochDay AND endEpochMillis IS NOT NULL")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Long> observeDayTotalMillis(long epochDay);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(endEpochMillis - startEpochMillis), 0) FROM non_wear_session WHERE dateEpochDay = :epochDay AND endEpochMillis IS NOT NULL")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDayTotalMillis(long epochDay, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertDailySummary(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM daily_non_wear_summary WHERE dateEpochDay = :epochDay LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDailySummary(long epochDay, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM daily_non_wear_summary ORDER BY dateEpochDay DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity>> observeRecentSummary(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM non_wear_session WHERE synced = 0 AND endEpochMillis IS NOT NULL")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnsyncedSessions(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.bracesaligner.core.database.entity.NonWearSessionEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE non_wear_session SET synced = 1 WHERE sessionId IN (:sessionIds)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsSynced(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> sessionIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}