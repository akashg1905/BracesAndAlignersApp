package com.example.bracesaligner.core.network.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/example/bracesaligner/core/network/api/TimerApi;", "", "getSummary", "Lcom/example/bracesaligner/core/network/dto/DailySummaryResponse;", "includeDaily", "", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncSession", "", "body", "Lcom/example/bracesaligner/core/network/dto/TimerSessionRequest;", "(Lcom/example/bracesaligner/core/network/dto/TimerSessionRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface TimerApi {
    
    @retrofit2.http.POST(value = "api/non-wear-sessions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object syncSession(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.dto.TimerSessionRequest body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @retrofit2.http.GET(value = "api/non-wear-sessions/summary")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSummary(@retrofit2.http.Query(value = "includeDaily")
    boolean includeDaily, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.network.dto.DailySummaryResponse> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}