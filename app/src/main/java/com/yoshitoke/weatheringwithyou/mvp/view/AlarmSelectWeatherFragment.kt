package com.yoshitoke.weatheringwithyou.mvp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.fragment.app.Fragment
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.mvp.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.fragment_alarm_select_weather.*

class AlarmSelectWeatherFragment(private val presenter: AlarmPresenter) : Fragment(){

    val checkedList = ArrayList<String>()

    companion object {
        fun getInstance(presenter: AlarmPresenter): Fragment {
            val alarmSelectWeatherFragment = AlarmSelectWeatherFragment(presenter)

            return alarmSelectWeatherFragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alarm_select_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_rainy.setOnClickListener(textListener)
        tv_swelter.setOnClickListener(textListener)
        tv_thunderstorm.setOnClickListener(textListener)

        save_btn.setOnClickListener{
            onSave()
            presenter.nextPage()
        }
    }

    private val textListener = View.OnClickListener {
        val textView = it as CheckedTextView
        textView.isChecked = !textView.isChecked

        if (textView.isChecked) {
            textView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background)
            checkedList.add(textView.text.toString())
        } else {
            textView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background)
            checkedList.remove(textView.text.toString())
        }
    }

    private fun onSave() {
        presenter.updateWeatherType(checkedList)
    }

}
