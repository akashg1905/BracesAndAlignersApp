package com.smylo.feature.profile.data

import com.smylo.core.network.api.SupportApi
import com.smylo.core.network.dto.CreateSupportQueryRequest
import com.smylo.core.network.dto.CreateSupportQueryResponse
import com.smylo.core.network.dto.SupportTopic
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupportRepository @Inject constructor(
    private val supportApi: SupportApi
) {
    suspend fun getSupportTopics(): List<SupportTopic> {
        return supportApi.getSupportTopics().topics
    }

    suspend fun createSupportQuery(topic: String, message: String, appVersion: String): CreateSupportQueryResponse {
        return supportApi.createSupportQuery(
            CreateSupportQueryRequest(
                topic = topic,
                message = message,
                appVersion = appVersion
            )
        )
    }
}
