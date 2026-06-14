package com.aabdeljaoued.digdone

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aabdeljaoued.digdone.data.UiState
import com.aabdeljaoued.digdone.ui.DigDoneApp
import com.aabdeljaoued.digdone.ui.theme.DigDoneTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val requestNotifications = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigDoneTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val context = LocalContext.current
                    val app = context.applicationContext as DigDoneApplication
                    val state by app.memoRepository.observeUiState().collectAsStateWithLifecycle(UiState())
                    LaunchedEffect(Unit) {
                        app.alarmScheduler.rescheduleAll()
                    }
                    DigDoneApp(
                        state = state,
                        onCreateMemo = { title, notes, firstDueAtMillis, recurrence ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !app.notificationHelper.areNotificationsAllowed()) {
                                requestNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            lifecycleScope.launch {
                                app.memoRepository.addMemo(title, notes, firstDueAtMillis, recurrence)
                                app.alarmScheduler.rescheduleAll()
                            }
                        },
                        onDeleteMemo = { id ->
                            lifecycleScope.launch {
                                app.memoRepository.deleteMemo(id)
                                app.alarmScheduler.cancel(id)
                                app.alarmScheduler.rescheduleAll()
                            }
                        },
                        onDisableNotifications = { phrase ->
                            val accepted = app.settingsRepository.disableNotificationsWithPhrase(phrase)
                            if (accepted) app.alarmScheduler.rescheduleAll()
                            accepted
                        },
                        onEnableNotifications = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !app.notificationHelper.areNotificationsAllowed()) {
                                requestNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            app.settingsRepository.setNotificationsEnabled(true)
                            app.alarmScheduler.rescheduleAll()
                        }
                    )
                }
            }
        }
    }
}
