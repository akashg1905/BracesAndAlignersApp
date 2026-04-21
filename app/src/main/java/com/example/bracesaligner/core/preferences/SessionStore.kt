package com.example.bracesaligner.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val authToken: Flow<String?> = dataStore.data.map { it[KEY_TOKEN] }
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val averageWearHours: Flow<Double> = dataStore.data.map { it[KEY_AVG_WEAR] ?: 0.0 }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[KEY_TOKEN] = token
            it[KEY_LOGGED_IN] = true
        }
    }

    suspend fun saveAverageWearHours(hours: Double) {
        dataStore.edit {
            it[KEY_AVG_WEAR] = hours
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.remove(KEY_TOKEN)
            it.remove(KEY_AVG_WEAR)
            it[KEY_LOGGED_IN] = false
        }
    }

    companion object {
        val KEY_TOKEN = stringPreferencesKey("session_token")
        val KEY_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_AVG_WEAR = doublePreferencesKey("avg_wear_hours")
    }
}
