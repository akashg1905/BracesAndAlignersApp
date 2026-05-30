package com.example.bracesaligner.feature.auth.data

import android.util.Log
import com.example.bracesaligner.core.database.AppDatabase
import com.example.bracesaligner.core.database.dao.AuthSessionDao
import com.example.bracesaligner.core.database.entity.AuthSessionEntity
import com.example.bracesaligner.core.network.JwtPayloadParser
import com.example.bracesaligner.core.network.api.AuthApi
import com.example.bracesaligner.core.network.dto.DeviceTokenRequest
import com.example.bracesaligner.core.network.dto.OtpSendRequest
import com.example.bracesaligner.core.network.dto.OtpVerifyRequest
import com.example.bracesaligner.core.network.dto.RefreshTokenRequest
import com.example.bracesaligner.core.preferences.SessionStore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        Log.i("AuthRepository", "verifyOtp called for email: $email")
        val response = authApi.verifyOtp(
            OtpVerifyRequest(email = email, phoneNumber = phoneNumber, otpCode = code)
        )
        Log.i("AuthRepository", "verifyOtp API success, userId: ${response.userId}")
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
        sessionStore.saveToken(response.accessToken, response.refreshToken)

        // Register device token for notifications after successful login
        Log.i("AuthRepository", "Login successful, preparing device registration...")
        try {
            val storedFcmToken = sessionStore.fcmToken.first()
            Log.i("AuthRepository", "Stored FCM Token: $storedFcmToken")
            
            val fcmToken = storedFcmToken ?: run {
                Log.i("AuthRepository", "FCM token not in store, fetching from Firebase...")
                fetchFcmTokenFromFirebase()
            }
            
            Log.i("AuthRepository", "Final FCM Token for registration: $fcmToken")
            if (!fcmToken.isNullOrBlank()) {
                registerDeviceToken(fcmToken)
            } else {
                Log.w("AuthRepository", "FCM Token is null or blank, skipping registration")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during device registration flow", e)
        }
    }

    private suspend fun fetchFcmTokenFromFirebase(): String? = suspendCoroutine { continuation ->
        Log.i("AuthRepository", "Fetching FCM token directly from FirebaseMessaging...")
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.i("AuthRepository", "FirebaseMessaging token fetch success: $token")
                    continuation.resume(token)
                } else {
                    Log.e("AuthRepository", "FirebaseMessaging token fetch failed", task.exception)
                    continuation.resume(null)
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Firebase initialization error (check google-services.json)", e)
            continuation.resume(null)
        }
    }

    suspend fun refreshTokens() {
        val currentRefreshToken = sessionStore.refreshToken.first() ?: return
        try {
            val response = authApi.refresh(RefreshTokenRequest(currentRefreshToken))
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
            sessionStore.saveToken(response.accessToken, response.refreshToken)
        } catch (e: Exception) {
            // If refresh fails, we might need to logout or handle it
            logout()
        }
    }

    fun observeLoggedIn(): Flow<Boolean> {
        return authSessionDao.observeSession().map { it?.isLoggedIn == true }
    }

    suspend fun isLoggedIn(): Boolean {
        return authSessionDao.getSession()?.isLoggedIn == true
    }

    suspend fun logout(fcmToken: String? = null) {
        withContext(Dispatchers.IO) {
            try {
                authApi.unregisterDevice(fcmToken)
            } catch (e: Exception) {
                // Ignore failure to ensure local logout still happens
            }
            // Clear Room Database tables
            database.clearAllTables()
            // Clear DataStore/SharedPreferences
            sessionStore.clear()
        }
    }

    suspend fun registerDeviceToken(token: String) {
        Log.i("AuthRepository", "Registering device token: $token")
        try {
            val deviceId = android.os.Build.ID
            val model = android.os.Build.MODEL
            val version = "1.0.0" 
            Log.i("AuthRepository", "Sending registerDevice request...")
            authApi.registerDevice(
                DeviceTokenRequest(
                    fcmToken = token,
                    deviceId = deviceId,
                    deviceModel = model,
                    appVersion = version
                )
            )
            Log.i("AuthRepository", "Device token registered successfully")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to register device token", e)
        }
    }
}
