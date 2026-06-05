package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.AlignerPlanResponse
import com.example.bracesaligner.core.network.dto.AlignerScheduleResponse
import com.example.bracesaligner.core.network.dto.CreatePlanRequest
import com.example.bracesaligner.core.network.dto.UpdatePlanScheduleRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AlignerPlanApi {
    @POST("api/plan")
    suspend fun createPlan(@Body body: CreatePlanRequest): AlignerPlanResponse

    @GET("api/plan/active")
    suspend fun getActivePlan(): Response<AlignerPlanResponse>

    @GET("api/plan/schedule")
    suspend fun getSchedule(): Response<AlignerScheduleResponse>

    @PATCH("api/plan/aligners")
    suspend fun updateSchedule(@Body body: UpdatePlanScheduleRequest): Response<Unit>
}
