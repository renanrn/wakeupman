package com.wakeupman.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wakeupman_prefs")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val EYE_OPEN_BASELINE = floatPreferencesKey("eye_open_baseline")
    }

    val eyeOpenBaselineFlow: Flow<Float?> = dataStore.data
        .map { preferences ->
            preferences[EYE_OPEN_BASELINE]
        }

    suspend fun saveEyeOpenBaseline(baseline: Float) {
        dataStore.edit { preferences ->
            preferences[EYE_OPEN_BASELINE] = baseline
        }
    }
}
