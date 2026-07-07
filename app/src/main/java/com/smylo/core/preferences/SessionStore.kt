package com.smylo.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val authToken: Flow<String?> = dataStore.data.map { it[KEY_TOKEN] }
    val refreshToken: Flow<String?> = dataStore.data.map { it[KEY_REFRESH_TOKEN] }
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val averageWearHours: Flow<Double> = dataStore.data.map { it[KEY_AVG_WEAR] ?: 0.0 }
    val averageWearDisplay: Flow<String> = dataStore.data.map { it[KEY_AVG_WEAR_DISPLAY] ?: "--" }
    val fcmToken: Flow<String?> = dataStore.data.map { it[KEY_FCM_TOKEN] }
    val lastDailyReminderDay: Flow<Long> = dataStore.data.map { it[KEY_LAST_DAILY_REMINDER] ?: 0L }

    suspend fun saveToken(accessToken: String, refreshToken: String? = null) {
        dataStore.edit {
            it[KEY_TOKEN] = accessToken
            if (refreshToken != null) {
                it[KEY_REFRESH_TOKEN] = refreshToken
            }
            it[KEY_LOGGED_IN] = true
        }
    }

    suspend fun saveFcmToken(token: String) {
        dataStore.edit {
            it[KEY_FCM_TOKEN] = token
        }
    }

    suspend fun saveAverageWearHours(hours: Double, display: String? = null) {
        dataStore.edit {
            it[KEY_AVG_WEAR] = hours
            if (display != null) {
                it[KEY_AVG_WEAR_DISPLAY] = display
            }
        }
    }

    suspend fun saveLastDailyReminderDay(day: Long) {
        dataStore.edit {
            it[KEY_LAST_DAILY_REMINDER] = day
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.remove(KEY_TOKEN)
            it.remove(KEY_REFRESH_TOKEN)
            it.remove(KEY_AVG_WEAR)
            it.remove(KEY_FCM_TOKEN)
            it[KEY_LOGGED_IN] = false
        }
    }

    companion object {
        val KEY_TOKEN = stringPreferencesKey("session_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val KEY_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_AVG_WEAR = doublePreferencesKey("avg_wear_hours")
        val KEY_AVG_WEAR_DISPLAY = stringPreferencesKey("avg_wear_display")
        val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
        val KEY_LAST_DAILY_REMINDER = longPreferencesKey("last_daily_reminder_day")
    }
}

