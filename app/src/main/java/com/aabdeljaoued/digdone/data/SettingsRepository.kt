package com.aabdeljaoued.digdone.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings_store")

data class SettingsState(
    val notificationsEnabled: Boolean = true,
    val notificationPhrase: String = "",
)

class SettingsRepository(private val context: Context) {
    private val enabledKey = booleanPreferencesKey("notifications_enabled")
    private val phraseKey = stringPreferencesKey("notification_phrase")

    fun observeSettings(): Flow<SettingsState> = context.settingsDataStore.data.map { prefs ->
        SettingsState(
            notificationsEnabled = prefs[enabledKey] ?: true,
            notificationPhrase = prefs[phraseKey] ?: "",
        )
    }

    suspend fun currentSettings(): SettingsState = observeSettings().first()

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs -> prefs[enabledKey] = enabled }
    }

    suspend fun disableNotificationsWithPhrase(phrase: String): Boolean {
        val entered = phrase.trim()
        if (entered.isBlank()) return false
        val current = currentSettings()
        val accepted = current.notificationPhrase.isBlank() || current.notificationPhrase == entered
        if (accepted) {
            context.settingsDataStore.edit { prefs ->
                prefs[enabledKey] = false
                prefs[phraseKey] = entered
            }
        }
        return accepted
    }
}
