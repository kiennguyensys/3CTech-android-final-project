package com.yoshitoke.weatheringwithyou.mvp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.alarm.NotificationHelper
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import com.yoshitoke.weatheringwithyou.mvp.presenter.AlarmPresenter
import com.yoshitoke.weatheringwithyou.utils.*
import kotlinx.android.synthetic.main.fragment_alarm_welcome.*
import kotlinx.android.synthetic.main.fragment_weather_details.*

class AlarmWelcomeFragment(private val presenter: AlarmPresenter) : Fragment(){

    companion object {
        fun getInstance(presenter: AlarmPresenter): Fragment {
            val alarmWelcomeFragment = AlarmWelcomeFragment(presenter)

            return alarmWelcomeFragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alarm_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        start_btn.setOnClickListener {
            presenter.nextPage()
        }
    }

}
