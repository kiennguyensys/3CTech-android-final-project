package com.yoshitoke.weatheringwithyou.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yoshitoke.weatheringwithyou.mvp.model.AlarmDatabaseHandler
import io.reactivex.android.schedulers.AndroidSchedulers

class BootReceiver : BroadcastReceiver() {
    val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals(ACTION_BOOT_COMPLETED)) {
            // Reschedule every alarm here
            val db = AlarmDatabaseHandler(context)
            val alarms = db.fetchAlarms()

            AlarmScheduler.scheduleAlarmsForReminder(context, alarms[0])
        }
    }
}
