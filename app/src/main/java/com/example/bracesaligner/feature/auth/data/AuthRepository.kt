package com.example.bracesaligner.feature.auth.data

import com.example.bracesaligner.core.database.AppDatabase
import com.example.bracesaligner.core.database.dao.AuthSessionDao
import com.example.bracesaligner.core.database.entity.AuthSessionEntity
import com.example.bracesaligner.core.network.JwtPayloadParser
import com.example.bracesaligner.core.network.api.AuthApi
import com.example.bracesaligner.core.network.dto.OtpSendRequest
import com.example.bracesaligner.core.network.dto.OtpVerifyRequest
import com.example.bracesaligner.core.preferences.SessionStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authSessionDao: AuthSessionDao,
    private val sessionStore: SessionStore,
    private val database: AppDatabase
) {
    suspend fun sendOtp(email: String, phoneNumber: String) {
        authApi.sendOtp(OtpSendRequest(email = email, phoneNumber = phoneNumber))
    }

    suspend fun verifyOtp(email: String, phoneNumber: String, code: String) {
        val response = authApi.verifyOtp(
            OtpVerifyRequest(email = email, phoneNumber = phoneNumber, otpCode = code)
        )
        val userId = response.userId?.takeIf { it.isNotBlank() }
            ?: JwtPayloadParser.parseSub(response.accessToken)
            ?: ""
        authSessionDao.upsertSession(
            AuthSessionEntity(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                userId = userId
            )
        )
        sessionStore.saveToken(response.accessToken)
    }

    fun observeLoggedIn(): Flow<Boolean> {
        return authSessionDao.observeSession().map { it?.isLoggedIn == true }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            // Clear Room Database tables
            database.clearAllTables()
            // Clear DataStore/SharedPreferences
            sessionStore.clear()
        }
    }
}
