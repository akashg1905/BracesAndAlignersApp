package com.smylo.feature.errors.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smylo.feature.errors.data.ClientErrorRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ClientErrorSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val clientErrorRepository: ClientErrorRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            clientErrorRepository.syncPendingErrors()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "client_error_sync"
    }
}
