package com.smylo.di

import com.smylo.BuildConfig
import com.smylo.core.database.AppDatabase
import com.smylo.core.network.api.AlignerPlanApi
import com.smylo.core.network.api.AuthApi
import com.smylo.core.network.api.ClientErrorApi
import com.smylo.core.network.api.NotificationApi
import com.smylo.core.network.api.TimerApi
import com.smylo.core.network.api.UserApi
import com.smylo.core.network.dto.RefreshTokenRequest
import com.smylo.core.preferences.SessionStore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private suspend fun readAccessToken(sessionStore: SessionStore, database: AppDatabase): String? {
        val fromStore = sessionStore.authToken.first()
        if (!fromStore.isNullOrBlank()) return fromStore
        return database.authSessionDao().getSession()?.accessToken?.takeIf { it.isNotBlank() }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionStore: SessionStore,
        database: AppDatabase
    ): Interceptor = Interceptor { chain ->
        val request = chain.request()
        val path = request.url.encodedPath

        // Skip adding Authorization header for auth-related endpoints
        val isAuthPath = path.contains("/auth/register") ||
            path.contains("/auth/login") ||
            path.contains("/auth/verify-otp") ||
            path.contains("/auth/refresh")

        val isPublicPath = isAuthPath || path.contains("/api/client-errors")

        val token = if (isPublicPath) null else runBlocking { readAccessToken(sessionStore, database) }

        val newRequest = request.newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }
        }.build()

        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun provideAuthenticator(
        sessionStore: SessionStore,
        authApiProvider: Provider<AuthApi>,
        database: AppDatabase
    ): Authenticator = object : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            // Only try to refresh if the response was 401
            if (response.code != 401) return null

            // Avoid infinite loops if refreshing also fails with 401
            if (response.request.url.encodedPath.contains("/auth/refresh")) {
                runBlocking {
                    database.clearAllTables()
                    sessionStore.clear()
                }
                return null
            }

            synchronized(this) {
                val currentToken = runBlocking { readAccessToken(sessionStore, database) }
                val refreshToken = runBlocking { sessionStore.refreshToken.first() }

                if (refreshToken.isNullOrBlank()) {
                    // Many backends only issue an access token (no refresh). Do not wipe the session
                    // on 401 — that looked like "login works then immediately fails" for /api/plan/active.
                    return null
                }

                // If the token has already been updated by another thread, use it
                val authHeader = response.request.header("Authorization")
                if (authHeader != null && currentToken != null && authHeader != "Bearer $currentToken") {
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                }

                return try {
                    val authApi = authApiProvider.get()
                    val refreshResponse = runBlocking {
                        authApi.refresh(RefreshTokenRequest(refreshToken))
                    }

                    runBlocking {
                        sessionStore.saveToken(
                            refreshResponse.accessToken,
                            refreshResponse.refreshToken
                        )
                    }

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                        .build()
                } catch (e: Exception) {
                    runBlocking {
                        database.clearAllTables()
                        sessionStore.clear()
                    }
                    null
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        authenticator: Authenticator
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAlignerPlanApi(retrofit: Retrofit): AlignerPlanApi =
        retrofit.create(AlignerPlanApi::class.java)

    @Provides
    @Singleton
    fun provideTimerApi(retrofit: Retrofit): TimerApi = retrofit.create(TimerApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi =
        retrofit.create(NotificationApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideClientErrorApi(retrofit: Retrofit): ClientErrorApi =
        retrofit.create(ClientErrorApi::class.java)
}

