package com.smylo.core.network.api

import com.smylo.core.network.dto.NotificationDispatchRequest
import com.smylo.core.network.dto.NotificationDispatchResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {
    @POST("/api/notifications/dispatch")
    suspend fun dispatchNotification(@Body request: List<NotificationDispatchRequest>): NotificationDispatchResponse
}

