package com.yoshitoke.weatheringwithyou.mvp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.alarm.AlarmData
import com.yoshitoke.weatheringwithyou.mvp.AlarmContract
import com.yoshitoke.weatheringwithyou.mvp.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.fragment_alarm_result.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmResultFragment(private val presenter: AlarmPresenter) : Fragment(), AlarmContract.View.FinalResultView{

    companion object {
        fun getInstance(presenter: AlarmPresenter): Fragment {
            val alarmResultFragment = AlarmResultFragment(presenter)

            return alarmResultFragment
        }
    }

    private val dateFormat = SimpleDateFormat("h:mm a",Locale.getDefault())

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alarm_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setResultView(this)
        edit_btn.setOnClickListener{
            presenter.switchToFirstPage()
        }
    }

    override fun onAlarmSet(alarmData: AlarmData) {
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, alarmData.hour as Int)
        date.set(Calendar.MINUTE, alarmData.minute as Int)
        textViewTime.text = dateFormat.format(date.time).toLowerCase(Locale.ROOT)

        var daysText = alarmData.days.toString()
        daysText = daysText.replace("[", "")
        daysText = daysText.replace("]", "")
        daysText = daysText.replace(",", " ·")
        daysText = daysText.replace("null", "")
        textViewDays.text = daysText
    }

}
