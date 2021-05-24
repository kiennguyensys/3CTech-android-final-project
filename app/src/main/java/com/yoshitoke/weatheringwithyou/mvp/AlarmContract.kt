package com.yoshitoke.weatheringwithyou.mvp

import android.location.Location
import com.yoshitoke.weatheringwithyou.alarm.AlarmData
import com.yoshitoke.weatheringwithyou.mvp.presenter.BasePresenter

interface AlarmContract {
    interface View {
        interface FinalResultView {
            fun onAlarmSet(alarmData: AlarmData)
        }

        fun onNextViewTransition()

        fun onSpecificViewTransition(position: Int)

        fun requestLocationPermission()

    }

    interface Presenter: BasePresenter {

        fun updateTime(hourOfDay: Int, minute: Int)

        fun updateDaysOfWeek(days: List<String>)

        fun updateWeatherType(checkedList: List<String>)

        fun confirmSetting()

        fun nextPage()

        fun checkPreviousSetting()

        fun switchToFirstPage()

        fun setResultView(view: View.FinalResultView)

        fun requestLocationPermission()

        fun updateLocation(location: Location)

    }
}
