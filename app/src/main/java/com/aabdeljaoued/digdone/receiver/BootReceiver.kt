package com.aabdeljaoued.digdone.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aabdeljaoued.digdone.DigDoneApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val app = context.applicationContext as DigDoneApplication
        goAsync().also { pendingResult ->
            CoroutineScope(Dispatchers.IO).launch {
                app.alarmScheduler.rescheduleAll()
                pendingResult.finish()
            }
        }
    }
}
