package com.aabdeljaoued.digdone.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun SettingsDialog(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onDisable: suspend (String) -> Boolean,
    onEnable: suspend () -> Unit,
) {
    var phrase by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                scope.launch {
                    if (enabled) {
                        val accepted = onDisable(phrase)
                        error = if (accepted) null else "Passphrase is required or does not match."
                        if (accepted) onDismiss()
                    } else {
                        onEnable()
                        onDismiss()
                    }
                }
            }) {
                Text(if (enabled) "Disable" else "Enable")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Close") } },
        title = { Text("Notification settings") },
        text = {
            if (enabled) {
                androidx.compose.foundation.layout.Column {
                    OutlinedTextField(value = phrase, onValueChange = { phrase = it }, label = { Text("Passphrase") })
                    if (error != null) Text(error!!)
                }
            } else {
                Text("Notifications are disabled.")
            }
        }
    )
}
