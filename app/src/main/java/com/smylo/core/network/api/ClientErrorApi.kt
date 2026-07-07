package com.smylo.core.network.api

import com.smylo.core.network.dto.ClientErrorsBatchRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ClientErrorApi {
    @POST("/api/client-errors")
    suspend fun submitErrors(@Body body: ClientErrorsBatchRequest)
}
