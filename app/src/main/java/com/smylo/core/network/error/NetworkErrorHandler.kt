package com.smylo.core.network.error

import com.smylo.core.common.ErrorMessenger
import com.smylo.feature.errors.data.ClientErrorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorHandler @Inject constructor(
    private val clientErrorRepository: ClientErrorRepository,
    private val errorMessenger: ErrorMessenger
) {
    /**
     * Maps [throwable] to a user-safe message, logs it locally, optionally shows a toast,
     * and queues upload to the backend when connectivity is available.
     */
    suspend fun report(
        throwable: Throwable,
        screen: String,
        endpoint: String? = null,
        showToast: Boolean = true,
        overrideUserMessage: String? = null
    ): String {
        val category = NetworkErrorMapper.categorize(throwable)
        val userMessage = overrideUserMessage ?: NetworkErrorMapper.toUserMessage(throwable)
        val technicalDetail = NetworkErrorMapper.toTechnicalDetail(throwable)
        val httpStatus = NetworkErrorMapper.httpStatus(throwable)

        clientErrorRepository.log(
            screen = screen,
            endpoint = endpoint,
            category = category,
            userMessage = userMessage,
            technicalDetail = technicalDetail,
            httpStatus = httpStatus
        )

        if (showToast) {
            errorMessenger.show(userMessage)
        }

        return userMessage
    }
}
