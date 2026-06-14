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

@Composable
fun SettingsDialog(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onDisable: (String) -> Unit,
    onEnable: () -> Unit,
) {
    var phrase by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { if (enabled) onDisable(phrase) else onEnable() }) {
                Text(if (enabled) "Disable" else "Enable")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Close") } },
        title = { Text("Notification settings") },
        text = {
            if (enabled) {
                OutlinedTextField(value = phrase, onValueChange = { phrase = it }, label = { Text("Passphrase") })
            } else {
                Text("Notifications are disabled.")
            }
        }
    )
}
