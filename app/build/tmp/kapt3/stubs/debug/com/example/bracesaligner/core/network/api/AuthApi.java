package com.example.bracesaligner.core.network.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\u0004\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/example/bracesaligner/core/network/api/AuthApi;", "", "sendOtp", "", "body", "Lcom/example/bracesaligner/core/network/dto/OtpSendRequest;", "(Lcom/example/bracesaligner/core/network/dto/OtpSendRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyOtp", "Lcom/example/bracesaligner/core/network/dto/AuthTokenResponse;", "Lcom/example/bracesaligner/core/network/dto/OtpVerifyRequest;", "(Lcom/example/bracesaligner/core/network/dto/OtpVerifyRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface AuthApi {
    
    @retrofit2.http.POST(value = "/auth/register")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendOtp(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.dto.OtpSendRequest body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @retrofit2.http.POST(value = "/auth/verify-otp")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verifyOtp(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.dto.OtpVerifyRequest body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bracesaligner.core.network.dto.AuthTokenResponse> $completion);
}