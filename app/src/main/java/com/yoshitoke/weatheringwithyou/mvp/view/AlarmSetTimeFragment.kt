package com.yoshitoke.weatheringwithyou.mvp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.Fragment
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.mvp.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.fragment_alarm_set_time.*
import kotlin.math.min


class AlarmSetTimeFragment(private val presenter: AlarmPresenter) : Fragment(){

    companion object {
        fun getInstance(presenter: AlarmPresenter): Fragment {
            val alarmSetTimeFragment = AlarmSetTimeFragment(presenter)

            return alarmSetTimeFragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alarm_set_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        timePicker_hours.setIs24HourView(true)

        timePicker_hours.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute -> presenter.updateTime(hourOfDay, minute) })

        buildCheckBoxes()

        next_btn.setOnClickListener {
            onSave()
            presenter.nextPage()
        }
    }

    private fun buildCheckBoxes() {
        linearLayoutDates.removeAllViews()
        val days = resources.getStringArray(R.array.days)
        for (day in days) {
            val checkBox = CheckBox(context)
            checkBox.text = day
            linearLayoutDates.addView(checkBox)
        }
    }

    private fun onSave() {
        val daysItems = resources.getStringArray(R.array.days)

        for (i in 0 until linearLayoutDates.childCount) {
            if (linearLayoutDates.getChildAt(i) is CheckBox) {
                val checkBox = linearLayoutDates.getChildAt(i) as CheckBox
                if (!checkBox.isChecked) {
                    daysItems[i] = null
                }
            }
        }

        presenter.updateDaysOfWeek(daysItems.toList())
        presenter.confirmSetting()
    }

}
