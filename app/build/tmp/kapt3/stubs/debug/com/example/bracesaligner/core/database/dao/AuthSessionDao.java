package com.example.bracesaligner.core.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\bH\'J\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/example/bracesaligner/core/database/dao/AuthSessionDao;", "", "clearSession", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSession", "Lcom/example/bracesaligner/core/database/entity/AuthSessionEntity;", "observeSession", "Lkotlinx/coroutines/flow/Flow;", "upsertSession", "session", "(Lcom/example/bracesaligner/core/database/entity/AuthSessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AuthSessionDao {
    
    @androidx.room.Query(value = "SELECT * FROM auth_session WHERE id = 1 LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.bracesaligner.core.database.entity.AuthSessionEntity> observeSession();
    
    @androidx.room.Query(value = "SELECT * FROM auth_session WHERE id = 1 LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.database.entity.AuthSessionEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertSession(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.entity.AuthSessionEntity session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM auth_session")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}