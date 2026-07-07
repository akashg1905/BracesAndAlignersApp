package com.smylo.core.network.api

import com.smylo.core.network.dto.DailySummaryResponse
import com.smylo.core.network.dto.TimerSessionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimerApi {
    @POST("api/non-wear-sessions")
    suspend fun syncSession(@Body body: TimerSessionRequest)

    @GET("api/non-wear-sessions/summary")
    suspend fun getSummary(
        @Query("planId") planId: String? = null,
        @Query("includeDaily") includeDaily: Boolean = false
    ): DailySummaryResponse
}

