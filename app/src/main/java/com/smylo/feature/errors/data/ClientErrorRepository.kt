package com.smylo.feature.errors.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.smylo.core.common.AppInfo
import com.smylo.core.database.dao.ClientErrorDao
import com.smylo.core.database.entity.ClientErrorEntity
import com.smylo.core.network.api.ClientErrorApi
import com.smylo.core.network.dto.ClientErrorReportDto
import com.smylo.core.network.dto.ClientErrorsBatchRequest
import com.smylo.core.network.error.ErrorCategory
import com.smylo.feature.errors.worker.ClientErrorSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientErrorRepository @Inject constructor(
    private val clientErrorDao: ClientErrorDao,
    private val clientErrorApi: ClientErrorApi,
    @ApplicationContext private val context: Context
) {
    suspend fun log(
        screen: String,
        endpoint: String?,
        category: ErrorCategory,
        userMessage: String,
        technicalDetail: String,
        httpStatus: Int?
    ) {
        clientErrorDao.insert(
            ClientErrorEntity(
                id = UUID.randomUUID().toString(),
                screen = screen,
                endpoint = endpoint,
                category = category.value,
                userMessage = userMessage,
                technicalDetail = technicalDetail,
                httpStatus = httpStatus,
                appVersion = AppInfo.versionName,
                occurredAtMillis = System.currentTimeMillis(),
                synced = false
            )
        )
        enqueueSync()
    }

    suspend fun syncPendingErrors(): Int {
        val pending = clientErrorDao.getUnsynced()
        if (pending.isEmpty()) return 0

        clientErrorApi.submitErrors(
            ClientErrorsBatchRequest(
                errors = pending.map { entity ->
                    ClientErrorReportDto(
                        screen = entity.screen,
                        endpoint = entity.endpoint,
                        category = entity.category,
                        userMessage = entity.userMessage,
                        technicalDetail = entity.technicalDetail,
                        httpStatus = entity.httpStatus,
                        appVersion = entity.appVersion,
                        occurredAtEpochMillis = entity.occurredAtMillis
                    )
                }
            )
        )

        clientErrorDao.deleteByIds(pending.map { it.id })
        return pending.size
    }

    fun enqueueSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<ClientErrorSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            ClientErrorSyncWorker.UNIQUE_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request
        )
    }
}
