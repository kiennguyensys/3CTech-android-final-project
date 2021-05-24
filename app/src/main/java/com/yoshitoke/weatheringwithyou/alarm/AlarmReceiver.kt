package com.yoshitoke.weatheringwithyou.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.yoshitoke.weatheringwithyou.R

class AlarmReceiver : BroadcastReceiver() {
    private val INTENT_EXTRA = "alarm"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action != null) {
            val json = intent.getStringExtra(INTENT_EXTRA)
            val alarm = Gson().fromJson(json, AlarmData::class.java)

            WeatherAlertHelper.checkCurrentWeatherForRegisteredAlarm(alarm, context)
        }
    }
}
