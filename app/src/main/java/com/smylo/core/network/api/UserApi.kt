package com.smylo.core.network.api
import com.smylo.core.network.dto.UpdateProfileRequest
import com.smylo.core.network.dto.UpdateUserSettingsRequest
import com.smylo.core.network.dto.SettingsCatalogResponse
import com.smylo.core.network.dto.UserProfileResponse
import com.smylo.core.network.dto.UserSettingsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody

interface UserApi {
    @GET("/api/users/me/profile")
    suspend fun getUserProfile(): UserProfileResponse

    @PATCH("/api/users/me/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): UserProfileResponse

    @Multipart
    @POST("/api/users/me/profile/image")
    suspend fun updateProfileImage(@Part image: MultipartBody.Part): UserProfileResponse

    @GET("/api/users/me/settings/catalog")
    suspend fun getSettingsCatalog(): SettingsCatalogResponse

    @GET("/api/users/me/settings")
    suspend fun getUserSettings(): UserSettingsResponse

    @PUT("/api/users/me/settings")
    suspend fun updateUserSettings(@Body body: UpdateUserSettingsRequest): UserSettingsResponse
}

