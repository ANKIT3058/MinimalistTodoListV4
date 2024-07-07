package com.darkblue.minimalisttodolistv4.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.darkblue.minimalisttodolistv4.data.model.ClockType
import com.darkblue.minimalisttodolistv4.data.model.FontFamilyType
import com.darkblue.minimalisttodolistv4.data.model.RecurrenceType
import com.darkblue.minimalisttodolistv4.data.model.SortType
import com.darkblue.minimalisttodolistv4.data.model.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "settings"

class AppPreferences private constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    companion object {
        @Volatile
        private var INSTANCE: AppPreferences? = null
        fun getInstance(context: Context): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppPreferences(context).also { INSTANCE = it }
            }
        }
    }

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val CLOCK_TYPE = stringPreferencesKey("clockType")
        val POST_NOTIFICATION_DENIAL_COUNT = intPreferencesKey("post_notification_denial_count")
        val FONT_FAMILY = stringPreferencesKey("fontFamily")
        val FONT_SIZE = intPreferencesKey("fontSize")
        val FONT_WEIGHT = stringPreferencesKey("fontWeight")
        val SORTING_OPTION = stringPreferencesKey("sorting_option")
        val RECURRENCE_FILTER = stringPreferencesKey("recurrence_option")
        val TUTORIAL_VISIBILITY = booleanPreferencesKey("tutorial_visibility")
    }

    val theme: Flow<ThemeType> = dataStore.data.map { preferences ->
        ThemeType.fromDisplayName(preferences[PreferencesKeys.THEME] ?: ThemeType.DARK.displayName)
    }

    val clockType: Flow<ClockType> = dataStore.data.map { preferences ->
        ClockType.fromDisplayName(preferences[PreferencesKeys.CLOCK_TYPE] ?: ClockType.TWELVE_HOUR.displayName)
    }

    val postNotificationDenialCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.POST_NOTIFICATION_DENIAL_COUNT] ?: 0
    }

    val fontFamily: Flow<FontFamilyType> = dataStore.data.map { preferences ->
        FontFamilyType.fromDisplayName(preferences[PreferencesKeys.FONT_FAMILY] ?: FontFamilyType.DEFAULT.displayName)
    }

    val fontSize: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FONT_SIZE] ?: 16
    }

    val fontWeight: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FONT_WEIGHT] ?: "Normal"
    }

    val priorityOption: Flow<SortType> = dataStore.data.map { preferences ->
        SortType.fromDisplayName(preferences[PreferencesKeys.SORTING_OPTION] ?: SortType.PRIORITY.displayName)
    }

    val recurrenceFilter: Flow<RecurrenceType> = dataStore.data.map { preferences ->
        RecurrenceType.fromDisplayName(preferences[PreferencesKeys.RECURRENCE_FILTER] ?: RecurrenceType.NONE.displayName)
    }

    val tutorialVisibility: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.TUTORIAL_VISIBILITY] ?: true
    }

    suspend fun saveTheme(theme: ThemeType) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.THEME] = theme.displayName }
    }

    suspend fun saveClockType(clockType: ClockType) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.CLOCK_TYPE] = clockType.displayName }
    }

    suspend fun incrementPostNotificationDenialCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.POST_NOTIFICATION_DENIAL_COUNT] ?: 0
            preferences[PreferencesKeys.POST_NOTIFICATION_DENIAL_COUNT] = currentCount + 1
        }
    }

    suspend fun resetPostNotificationDenialCount() {
        dataStore.edit { preferences -> preferences[PreferencesKeys.POST_NOTIFICATION_DENIAL_COUNT] = 0 }
    }

    suspend fun saveFontFamily(fontFamily: FontFamilyType) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.FONT_FAMILY] = fontFamily.displayName }
    }

    suspend fun saveFontSize(fontSize: Int) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.FONT_SIZE] = fontSize }
    }

    suspend fun saveFontWeight(fontWeight: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.FONT_WEIGHT] = fontWeight }
    }

    suspend fun savePriority(priority: SortType) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SORTING_OPTION] = priority.displayName }
    }

    suspend fun saveRecurrence(recurrenceFilter: RecurrenceType) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.RECURRENCE_FILTER] = recurrenceFilter.displayName }
    }

    suspend fun disableTutorialDialog() {
        dataStore.edit { preferences -> preferences[PreferencesKeys.TUTORIAL_VISIBILITY] = false }
    }
}