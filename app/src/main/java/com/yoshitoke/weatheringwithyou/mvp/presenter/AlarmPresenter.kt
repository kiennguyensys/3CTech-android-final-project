package com.yoshitoke.weatheringwithyou.mvp.presenter

import android.content.Context
import android.location.Location
import android.os.Handler
import android.util.Log
import com.yoshitoke.weatheringwithyou.alarm.AlarmData
import com.yoshitoke.weatheringwithyou.alarm.AlarmScheduler
import com.yoshitoke.weatheringwithyou.alarm.NotificationHelper
import com.yoshitoke.weatheringwithyou.mvp.AlarmContract
import com.yoshitoke.weatheringwithyou.mvp.model.AlarmDatabaseHandler

class AlarmPresenter(
        private var mainView: AlarmContract.View?,
        private val context: Context
        ) : AlarmContract.Presenter {

    var mDays: List<String>? = null
    var mHour: Int? = null
    var mMinute: Int? = null
    var mWeatherList: List<String>? = null
    var alarmData: AlarmData? = null
    var finalResultView: AlarmContract.View.FinalResultView? = null

    override fun destroy() = Unit

    override fun updateDaysOfWeek(days: List<String>) {
        mDays = days
    }

    override fun updateTime(hourOfDay: Int, minute: Int) {
        mHour = hourOfDay
        mMinute = minute
    }

    override fun updateWeatherType(checkedList: List<String>) {
        mWeatherList = checkedList
    }

    override fun confirmSetting() {
        val db = AlarmDatabaseHandler(context)

        if(alarmData == null) {
            alarmData = AlarmData(1, mWeatherList, mHour, mMinute, mDays)
            db.createAlarm(alarmData as AlarmData)
            AlarmScheduler.scheduleAlarmsForReminder(context, alarmData as AlarmData)
        } else {
            alarmData = AlarmData(1, mWeatherList, mHour, mMinute, mDays)
            db.updateAlarm(alarmData as AlarmData)
            AlarmScheduler.updateAlarmsForReminder(context, alarmData as AlarmData)
        }

        finalResultView?.onAlarmSet(alarmData as AlarmData)
    }

    override fun checkPreviousSetting() {
        val db = AlarmDatabaseHandler(context)
        val alarms = db.fetchAlarms()

        if (alarms.isNotEmpty()) {
            alarmData = alarms[0]
            mainView?.onSpecificViewTransition(3)

            Handler().postDelayed({
                finalResultView?.onAlarmSet(alarmData as AlarmData)
            }, 2000)
        }
    }

    override fun switchToFirstPage() {
        mainView?.onSpecificViewTransition(0)
    }

    override fun nextPage() {
        mainView?.onNextViewTransition()
    }

    override fun setResultView(view: AlarmContract.View.FinalResultView) {
        finalResultView = view
    }

    override fun requestLocationPermission() {
        mainView?.requestLocationPermission()
    }

    override fun updateLocation(location: Location) {
        TODO("update to SharedPreferences with CURRENT_GPS KEY")
    }
}
