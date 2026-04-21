package com.example.bracesaligner.core.network.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bH\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\f"}, d2 = {"Lcom/example/bracesaligner/core/network/api/AlignerPlanApi;", "", "createPlan", "Lcom/example/bracesaligner/core/network/dto/AlignerPlanResponse;", "body", "Lcom/example/bracesaligner/core/network/dto/CreatePlanRequest;", "(Lcom/example/bracesaligner/core/network/dto/CreatePlanRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActivePlan", "Lretrofit2/Response;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSchedule", "Lcom/example/bracesaligner/core/network/dto/AlignerScheduleResponse;", "app_debug"})
public abstract interface AlignerPlanApi {
    
    @retrofit2.http.POST(value = "api/plan")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createPlan(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.dto.CreatePlanRequest body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.network.dto.AlignerPlanResponse> $completion);
    
    @retrofit2.http.GET(value = "api/plan/active")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActivePlan(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.bracesaligner.core.network.dto.AlignerPlanResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/plan/schedule")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSchedule(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.bracesaligner.core.network.dto.AlignerScheduleResponse>> $completion);
}