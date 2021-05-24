package com.yoshitoke.weatheringwithyou.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.yoshitoke.weatheringwithyou.R
import java.util.*

object AlarmScheduler {

    const val INTENT_ALARM_EXTRA = "alarm"
    const val INTENT_ID_EXTRA = "id"

    fun scheduleAlarmsForReminder(context: Context, alarmData: AlarmData) {

        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Schedule the alarms based on the days to administer the medicine
        val days = context.resources.getStringArray(R.array.days)
        if (alarmData.days != null) {
            for (day in alarmData.days as List<String>) {
                if (day != null) {
                    // get the PendingIntent for the alarm
                    val alarmIntent = createPendingIntent(context, alarmData, day)
                    // schedule the alarm
                    val dayOfWeek = getDayOfWeek(days, day)
                    scheduleAlarm(alarmData, dayOfWeek, alarmIntent, alarmMgr)
                }
            }
        }
    }

    /**
     * Schedules a single alarm
     */
    private fun scheduleAlarm(alarmData: AlarmData, dayOfWeek: Int, alarmIntent: PendingIntent?, alarmMgr: AlarmManager) {

        // Set up the time to schedule the alarm
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, alarmData.hour as Int)
        datetimeToAlarm.set(Calendar.MINUTE, alarmData.minute as Int)
        datetimeToAlarm.set(Calendar.SECOND, 0)
        datetimeToAlarm.set(Calendar.MILLISECOND, 0)
        datetimeToAlarm.set(Calendar.DAY_OF_WEEK, dayOfWeek)

        // Compare the datetimeToAlarm to today
        val today = Calendar.getInstance(Locale.getDefault())
        if (shouldNotifyToday(dayOfWeek, today, datetimeToAlarm)) {

            // schedule for today
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    datetimeToAlarm.timeInMillis, alarmIntent)
            return
        }

        // schedule 1 week out from the day
        datetimeToAlarm.roll(Calendar.WEEK_OF_YEAR, 1)
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent)
    }

    /**
     * Creates a [PendingIntent] for the Alarm using the [AlarmData]
     *
     * @param context      current application context
     * @param alarmData AlarmData for the notification
     * @param day          String representation of the day
     */
    private fun createPendingIntent(context: Context, alarmData: AlarmData, day: String?): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_notify_weather_alert)
            type = "$day-${alarmData.weatherTypes}"
            putExtra(INTENT_ALARM_EXTRA, Gson().toJson(alarmData))
        }

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Determines if the Alarm should be scheduled for today.
     *
     * @param dayOfWeek day of the week as an Int
     * @param today today's datetime
     * @param datetimeToAlarm Alarm's datetime
     */
    private fun shouldNotifyToday(dayOfWeek: Int, today: Calendar, datetimeToAlarm: Calendar): Boolean {
        return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                today.get(Calendar.HOUR_OF_DAY) <= datetimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= datetimeToAlarm.get(Calendar.MINUTE)
    }

    /**
     * Updates a notification.
     * Note: this just calls [AlarmScheduler.scheduleAlarmsForReminder] since
     * alarms with exact matching pending intents will update if they are already set, otherwise
     * call [AlarmScheduler.removeAlarmsForReminder] if the medicine has been administered.
     *
     * @param context      current application context
     * @param alarmData AlarmData for the notification
     */
    fun updateAlarmsForReminder(context: Context, alarmData: AlarmData) {
        if (!alarmData.administered) {
            AlarmScheduler.scheduleAlarmsForReminder(context, alarmData)
        } else {
            AlarmScheduler.removeAlarmsForReminder(context, alarmData)
        }
    }

    /**
     * Removes the notification if it was previously scheduled.
     *
     * @param context      current application context
     * @param alarmData AlarmData for the notification
     */
    fun removeAlarmsForReminder(context: Context, alarmData: AlarmData) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_weather_alert)
        intent.putExtra(INTENT_ID_EXTRA, alarmData.id)

        // type must be unique so Intent.filterEquals passes the check to make distinct PendingIntents
        // Schedule the alarms based on the days to administer the medicine
        if (alarmData.days != null) {
            for (day in alarmData.days as List<String>) {
                if(day != null) {
                    val type = String.format(Locale.getDefault(), "%s-%s", day, alarmData.weatherTypes)

                    intent.type = type
                    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmMgr.cancel(alarmIntent)
                }
            }
        }
    }

    /**
     * Returns the int representation for the day of the week.
     *
     * @param days      array from resources
     * @param dayOfWeek String representation of the day e.g "Sunday"
     * @return [Calendar.DAY_OF_WEEK] for given dayOfWeek
     */
    private fun getDayOfWeek(days: Array<String>, dayOfWeek: String): Int {
        return when {
            dayOfWeek.equals(days[0], ignoreCase = true) -> Calendar.SUNDAY
            dayOfWeek.equals(days[1], ignoreCase = true) -> Calendar.MONDAY
            dayOfWeek.equals(days[2], ignoreCase = true) -> Calendar.TUESDAY
            dayOfWeek.equals(days[3], ignoreCase = true) -> Calendar.WEDNESDAY
            dayOfWeek.equals(days[4], ignoreCase = true) -> Calendar.THURSDAY
            dayOfWeek.equals(days[5], ignoreCase = true) -> Calendar.FRIDAY
            else -> Calendar.SATURDAY
        }
    }

}
