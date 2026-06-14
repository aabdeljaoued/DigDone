package com.aabdeljaoued.digdone.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aabdeljaoued.digdone.model.Recurrence

@Composable
fun CreateMemoDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, Long, Recurrence) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var dueInMinutes by remember { mutableStateOf("5") }
    var recurrence by remember { mutableStateOf(Recurrence.DAILY) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val minutes = dueInMinutes.toLongOrNull()?.coerceAtLeast(1L) ?: 5L
                onCreate(title, notes, System.currentTimeMillis() + minutes * 60_000L, recurrence)
            }) {
                Text("Create")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("New memo") },
        text = {
            androidx.compose.foundation.layout.Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") })
                OutlinedTextField(value = dueInMinutes, onValueChange = { dueInMinutes = it }, label = { Text("First reminder in minutes") })
                Recurrence.values().forEach { item ->
                    Button(onClick = { recurrence = item }) {
                        Text(if (item == recurrence) "Selected: $item" else item.name)
                    }
                }
            }
        }
    )
}
