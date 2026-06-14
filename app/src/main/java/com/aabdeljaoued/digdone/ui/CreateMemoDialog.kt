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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeParseException

@Composable
fun CreateMemoDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, Long, Recurrence) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var dueTime by remember { mutableStateOf(LocalTime.now().plusMinutes(5).withSecond(0).withNano(0).toString()) }
    var recurrence by remember { mutableStateOf(Recurrence.DAILY) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val trimmedTitle = title.trim()
                if (trimmedTitle.isBlank()) {
                    error = "Title is required."
                    return@Button
                }
                try {
                    val dueAt = LocalDateTime.of(LocalDate.parse(dueDate), LocalTime.parse(dueTime))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    if (dueAt <= System.currentTimeMillis()) {
                        error = "First reminder must be in the future."
                        return@Button
                    }
                    onCreate(trimmedTitle, notes.trim(), dueAt, recurrence)
                } catch (_: DateTimeParseException) {
                    error = "Use date YYYY-MM-DD and time HH:MM."
                }
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
                OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Date YYYY-MM-DD") })
                OutlinedTextField(value = dueTime, onValueChange = { dueTime = it }, label = { Text("Time HH:MM") })
                Recurrence.values().forEach { item ->
                    Button(onClick = { recurrence = item }) {
                        Text(if (item == recurrence) "Selected: $item" else item.name)
                    }
                }
                if (error != null) Text(error!!)
            }
        }
    )
}
