package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.DailySummaryResponse
import com.example.bracesaligner.core.network.dto.TimerSessionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimerApi {
    @POST("api/non-wear-sessions")
    suspend fun syncSession(@Body body: TimerSessionRequest)

    @GET("api/non-wear-sessions/summary")
    suspend fun getSummary(@Query("includeDaily") includeDaily: Boolean = false): DailySummaryResponse
}
