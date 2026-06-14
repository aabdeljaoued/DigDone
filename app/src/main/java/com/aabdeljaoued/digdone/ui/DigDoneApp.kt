package com.aabdeljaoued.digdone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aabdeljaoued.digdone.data.UiState
import com.aabdeljaoued.digdone.model.Recurrence

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigDoneApp(
    state: UiState,
    onCreateMemo: (String, String, Long, Recurrence) -> Unit,
    onDeleteMemo: (String) -> Unit,
    onDisableNotifications: (String) -> Unit,
    onEnableNotifications: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showCreate by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("DigDone") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreate = true }) { Text("+") }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { showSettings = true }) { Text(if (state.notificationsEnabled) "Disable notifications" else "Enable notifications") }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                items(state.memos, key = { it.id }) { memo ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(memo.title, style = MaterialTheme.typography.titleMedium)
                            Text(memo.notes.ifBlank { "No notes" })
                            Text("Recurrence: ${memo.recurrence}")
                            IconButton(onClick = { onDeleteMemo(memo.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete memo")
                            }
                        }
                    }
                }
            }
        }
    }
    if (showSettings) {
        SettingsDialog(
            enabled = state.notificationsEnabled,
            onDismiss = { showSettings = false },
            onDisable = { phrase ->
                onDisableNotifications(phrase)
                showSettings = false
            },
            onEnable = {
                onEnableNotifications()
                showSettings = false
            }
        )
    }
    if (showCreate) {
        CreateMemoDialog(onDismiss = { showCreate = false }, onCreate = { title, notes, dueAt, recurrence ->
            showCreate = false
            onCreateMemo(title, notes, dueAt, recurrence)
        })
    }
}
