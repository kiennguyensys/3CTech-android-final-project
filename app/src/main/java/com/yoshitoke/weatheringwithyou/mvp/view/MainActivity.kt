package com.yoshitoke.weatheringwithyou.mvp.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.mvp.Contract
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import com.yoshitoke.weatheringwithyou.mvp.model.Model
import com.yoshitoke.weatheringwithyou.mvp.presenter.MainPresenter
import com.yoshitoke.weatheringwithyou.utils.kelvinToCelsius
import com.yoshitoke.weatheringwithyou.utils.unixTimestampToString
import kotlinx.android.synthetic.main.current_conditions_layout.*
import kotlinx.android.synthetic.main.location_picking_layout.*

class MainActivity : AppCompatActivity(), Contract.View, AdapterView.OnItemSelectedListener {
    var mPresenter: Contract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainPresenter(this, Model(), applicationContext)

        mPresenter?.loadCityList()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        mPresenter?.onLocationSwitched(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun showSpinnerList(data: List<String>) {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_dropdown_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        location_spinner.setAdapter(adapter)
        location_spinner.onItemSelectedListener = this
    }

    override fun showWeatherInfo(data: WeatherInfo) {
        val tempText = data.current.temperature.kelvinToCelsius().toString() + "°C"
        tv_temperature.setText(tempText)

        tv_condition.setText(data.current.weathers[0].description)
        tv_dateTime.setText(data.current.dateTime.unixTimestampToString("dd MMM, yyyy - hh:mm a"))
    }

    override fun onDestroy() {
        mPresenter?.destroyView()
        super.onDestroy()
    }
}