package com.aabdeljaoued.digdone.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.aabdeljaoued.digdone.data.MemoRepository
import com.aabdeljaoued.digdone.model.Memo
import com.aabdeljaoued.digdone.model.nextDue
import com.aabdeljaoued.digdone.notify.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AlarmScheduler(
    private val context: Context,
    private val memoRepository: MemoRepository,
    private val notificationHelper: NotificationHelper,
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    suspend fun rescheduleAll() = withContext(Dispatchers.IO) {
        memoRepository.observeMemos().first().filter { it.isActive }.forEach { schedule(it) }
    }

    suspend fun schedule(memo: Memo) = withContext(Dispatchers.IO) {
        notificationHelper.ensureChannel()
        val intent = Intent(context, com.aabdeljaoued.digdone.receiver.MemoAlarmReceiver::class.java)
            .setAction(ACTION_MEMO_ALARM)
            .putExtra(EXTRA_MEMO_ID, memo.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            memo.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(pendingIntent)
        val triggerAt = memo.firstDueAtMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return@withContext
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
    }

    companion object {
        const val ACTION_MEMO_ALARM = "com.aabdeljaoued.digdone.ACTION_MEMO_ALARM"
        const val EXTRA_MEMO_ID = "extra_memo_id"
    }
}
