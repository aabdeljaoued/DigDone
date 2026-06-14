package com.aabdeljaoued.digdone.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aabdeljaoued.digdone.DigDoneApplication
import com.aabdeljaoued.digdone.model.nextDue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as DigDoneApplication
        val memoId = intent.getStringExtra(com.aabdeljaoued.digdone.schedule.AlarmScheduler.EXTRA_MEMO_ID) ?: return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val memo = app.memoRepository.memoById(memoId)
            val settings = app.settingsRepository.currentSettings()
            if (memo != null) {
                if (settings.notificationsEnabled) {
                    app.notificationHelper.showMemoNotification(memo.title, memo.notes.ifBlank { "Memo is due." }, memoId.hashCode())
                }
                val nextDueAt = memo.recurrence.nextDue(memo.firstDueAtMillis)
                app.memoRepository.updateMemoDue(memo.id, nextDueAt)
                app.alarmScheduler.schedule(memo.copy(firstDueAtMillis = nextDueAt))
            }
            pendingResult.finish()
        }
    }
}
