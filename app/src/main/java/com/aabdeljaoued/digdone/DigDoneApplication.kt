package com.aabdeljaoued.digdone

import android.app.Application
import com.aabdeljaoued.digdone.data.MemoRepository
import com.aabdeljaoued.digdone.data.SettingsRepository
import com.aabdeljaoued.digdone.notify.NotificationHelper
import com.aabdeljaoued.digdone.schedule.AlarmScheduler

class DigDoneApplication : Application() {
    val settingsRepository by lazy { SettingsRepository(this) }
    val notificationHelper by lazy { NotificationHelper(this) }
    val memoRepository by lazy {
        MemoRepository(
            this,
            notificationPermissionGranted = { notificationHelper.areNotificationsAllowed() },
        )
    }
    val alarmScheduler by lazy { AlarmScheduler(this, memoRepository, notificationHelper) }
}
