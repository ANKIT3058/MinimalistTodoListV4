package com.darkblue.minimalisttodolistv4.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PREFERENCES_NAME = "settings"

class AppPreferences(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    private val THEME_KEY = stringPreferencesKey("theme")
    private val CLOCK_TYPE_KEY = stringPreferencesKey("clockType")
    private val POST_NOTIFICATION_DENIAL_COUNT = intPreferencesKey("post_notification_denial_count")
    val theme: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: "Dark"
        }
    val clockType: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[CLOCK_TYPE_KEY] ?: "12" // Default to 12-hour clock
        }
    val postNotificationDenialCount: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[POST_NOTIFICATION_DENIAL_COUNT] ?: 0
        }

    suspend fun saveTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    suspend fun saveClockType(clockType: String) {
        dataStore.edit { preferences ->
            preferences[CLOCK_TYPE_KEY] = clockType
        }
    }
    suspend fun incrementPostNotificationDenialCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[POST_NOTIFICATION_DENIAL_COUNT] ?: 0
            preferences[POST_NOTIFICATION_DENIAL_COUNT] = currentCount + 1
        }
    }
    suspend fun resetPostNotificationDenialCount() {
        dataStore.edit { preferences ->
            preferences[POST_NOTIFICATION_DENIAL_COUNT] = 0
        }
    }
}