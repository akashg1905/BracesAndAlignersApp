package com.example.bracesaligner.core.network.api

import com.example.bracesaligner.core.network.dto.NotificationDispatchRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {
    @POST("/api/notifications/dispatch")
    suspend fun dispatchNotification(@Body request: List<NotificationDispatchRequest>)
}
