package com.yoshitoke.weatheringwithyou.mvp.view

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.alarm.NotificationHelper
import com.yoshitoke.weatheringwithyou.mvp.Contract
import com.yoshitoke.weatheringwithyou.mvp.model.CityDatabaseHandler
import com.yoshitoke.weatheringwithyou.mvp.model.CityDatabaseHandler.Companion.CITY_LAT
import com.yoshitoke.weatheringwithyou.mvp.model.CityDatabaseHandler.Companion.CITY_LON
import com.yoshitoke.weatheringwithyou.mvp.model.CityDatabaseHandler.Companion.CITY_NAME
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.City
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.Hourly
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import com.yoshitoke.weatheringwithyou.mvp.presenter.MainPresenter
import com.yoshitoke.weatheringwithyou.utils.kelvinToCelsius
import com.yoshitoke.weatheringwithyou.utils.toCelsiusString
import com.yoshitoke.weatheringwithyou.utils.toFirstCapString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_tabs_layout.*
import kotlinx.android.synthetic.main.current_conditions_layout.*
import kotlinx.android.synthetic.main.hourly_forecast_layout.*
import kotlinx.android.synthetic.main.location_picking_layout.*


class MainActivity : AppCompatActivity(), Contract.View, AdapterView.OnItemSelectedListener {

    companion object {
        var mPresenter: Contract.Presenter? = null
        private lateinit var mAdapter: HourlyTimeLineAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
        private val REQUEST_CODE_MAPS = 0x9345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMoreButtonListener()
        mapIcon.setOnClickListener{ openMap() }
        alarmSettingButton.setOnClickListener { openAlarmSetting() }

        mPresenter = MainPresenter(this, applicationContext)
        mPresenter?.loadCityList()

        NotificationHelper.createNotificationChannel(this,
                NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                getString(R.string.app_name), "App notification channel.")
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        mPresenter?.switchLocation(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        TODO("view unchanged")
    }

    override fun showSpinnerList(data: List<City>) {
        val cityAdapterObj = CityAdapter(this, data)

        location_spinner.setAdapter(cityAdapterObj)
        location_spinner.onItemSelectedListener = this
    }

    override fun showWeatherInfo(data: WeatherInfo) {
        val tempText = data.current.temperature.kelvinToCelsius().toCelsiusString()
        tv_temperature.setText(tempText)

        tv_dateTime.setText("Now")
        tv_condition.setText(data.current.weathers[0].description.toFirstCapString())

        showHourlyForecast(data.hours)
        initBottomTabs(data)
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_MAPS) {

            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra(MapsMarkerActivity.CITY_DATA)
                val cityObj = Gson().fromJson(result, City::class.java)

                //add local city to database
                val cityDB = CityDatabaseHandler(this)

                val values = ContentValues()
                values.put(CITY_NAME, cityObj.name)
                values.put(CITY_LAT, cityObj.latitude)
                values.put(CITY_LON, cityObj.longitude)

                cityDB.AddCity(values)

                //reload list
                mPresenter?.loadCityList()

            } else {
                TODO("OTHER REQUEST")
            }
        }
    }

    override fun onDestroy() {
        mPresenter?.destroy()
        super.onDestroy()
    }

    private fun showHourlyForecast(data: List<Hourly>) {
        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = HourlyTimeLineAdapter(data)
        recyclerView.adapter = mAdapter
    }

    private fun initBottomTabs(data: WeatherInfo) {
        val viewPagerAdapter = ViewPager2Adapter(this, 2, data)
        pager.adapter = viewPagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.daily_forecast_tab)
                1 -> tab.text = getString(R.string.additional_info_tab)
            }
        }.attach()
    }

    private fun initMoreButtonListener() {
        tabsOpenBtn.setOnClickListener {
            if(bottom_tabs.visibility == View.GONE) {
                bottom_tabs.apply {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_up))
                    visibility = View.VISIBLE
                }
            } else {
                bottom_tabs.apply {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_down))
                    visibility = View.GONE
                }
            }
        }
    }

    private fun openMap() {
        val intent = Intent(this, MapsMarkerActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_MAPS)
    }

    private fun openAlarmSetting() {
        val intent = Intent(this, AlarmActivity::class.java)
        startActivity(intent)
    }
}
