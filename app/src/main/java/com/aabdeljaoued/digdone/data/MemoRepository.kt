package com.aabdeljaoued.digdone.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aabdeljaoued.digdone.model.Memo
import com.aabdeljaoued.digdone.model.Recurrence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.UUID

private val Context.memoDataStore by preferencesDataStore(name = "memo_store")

class MemoRepository(
    private val context: Context,
    private val notificationPermissionGranted: () -> Boolean = { true },
) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val memosKey = stringPreferencesKey("memos")

    fun observeUiState(): Flow<UiState> = combine(observeMemos(), SettingsRepository(context).observeSettings()) { memos, settings ->
        UiState(
            memos = memos.sortedBy { it.firstDueAtMillis },
            notificationsEnabled = settings.notificationsEnabled,
            notificationPhrase = settings.notificationPhrase,
            notificationPermissionGranted = notificationPermissionGranted(),
        )
    }

    fun observeMemos(): Flow<List<Memo>> = context.memoDataStore.data.map { prefs ->
        prefs[memosKey]?.let { json.decodeFromString<List<Memo>>(it) } ?: emptyList()
    }

    suspend fun currentMemos(): List<Memo> = observeMemos().first()

    suspend fun addMemo(title: String, notes: String, firstDueAtMillis: Long, recurrence: Recurrence) {
        context.memoDataStore.edit { prefs ->
            val current = readMemos(prefs).toMutableList()
            current.add(
                Memo(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    notes = notes,
                    recurrence = recurrence,
                    firstDueAtMillis = firstDueAtMillis,
                )
            )
            prefs[memosKey] = json.encodeToString(current)
        }
    }

    suspend fun deleteMemo(id: String) {
        context.memoDataStore.edit { prefs ->
            prefs[memosKey] = json.encodeToString(readMemos(prefs).filterNot { it.id == id })
        }
    }

    suspend fun updateMemoDue(id: String, newDueAtMillis: Long) {
        context.memoDataStore.edit { prefs ->
            val updated = readMemos(prefs).map { memo ->
                if (memo.id == id) memo.copy(firstDueAtMillis = newDueAtMillis) else memo
            }
            prefs[memosKey] = json.encodeToString(updated)
        }
    }

    suspend fun memoById(id: String): Memo? = currentMemos().firstOrNull { it.id == id }

    private fun readMemos(prefs: Preferences): List<Memo> = prefs[memosKey]?.let { json.decodeFromString(it) } ?: emptyList()
}
