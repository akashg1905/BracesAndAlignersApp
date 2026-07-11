package com.smylo.core.network.api

import com.smylo.core.network.dto.CreateSupportQueryRequest
import com.smylo.core.network.dto.CreateSupportQueryResponse
import com.smylo.core.network.dto.SupportTopicsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SupportApi {
    @GET("/api/support/topics/catalog")
    suspend fun getSupportTopics(): SupportTopicsResponse

    @POST("/api/support/queries")
    suspend fun createSupportQuery(@Body body: CreateSupportQueryRequest): CreateSupportQueryResponse
}
