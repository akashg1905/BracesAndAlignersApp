package com.example.bracesaligner.di

import com.example.bracesaligner.BuildConfig
import com.example.bracesaligner.core.database.AppDatabase
import com.example.bracesaligner.core.network.api.AlignerPlanApi
import com.example.bracesaligner.core.network.api.AuthApi
import com.example.bracesaligner.core.network.api.TimerApi
import com.example.bracesaligner.core.preferences.SessionStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionStore: SessionStore,
        database: AppDatabase
    ): Interceptor = Interceptor { chain ->
        val request = chain.request()
        val path = request.url.encodedPath

        // Skip adding Authorization header for auth-related endpoints
        val isAuthPath = path.contains("/auth/register") || path.contains("/auth/verify-otp")
        
        val token = if (isAuthPath) null else runBlocking { sessionStore.authToken.first() }
        
        val newRequest = request.newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        
        val response = chain.proceed(newRequest)

        // Handle 401 Unauthorized by clearing session and database
        if (response.code == 401 && !isAuthPath) {
            runBlocking {
                database.clearAllTables()
                sessionStore.clear()
            }
        }
        
        response
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
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
}
