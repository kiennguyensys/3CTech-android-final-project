package com.yoshitoke.weatheringwithyou.alarm

import android.content.Context
import android.util.Log
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.City
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import com.yoshitoke.weatheringwithyou.utils.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object WeatherAlertHelper {
    //Test city with rain
    //We also can get current GPS from SharedPreferences with CURRENT_GPS key
    var bruneiTest: City = City("Brunei",  114.949008710682, 4.8977738654898)


    fun checkCurrentWeatherForRegisteredAlarm(alarmData: AlarmData, context: Context) {
        val disposable = RetrofitClient.weatherService.getWeatherInfo(bruneiTest.latitude.toString(), bruneiTest.longitude.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> onHandleResponse(result, alarmData, context)},
                        {error -> Log.e("WeatherAlertHelper: ", error.message.toString())}
                )
    }

    private fun onHandleResponse(result: WeatherInfo, alarmData: AlarmData, context: Context) {
        val currentWeather = result.current.weathers[0].main
        val alertList = alarmData.weatherTypes as List<String>

        if (alertList.contains(currentWeather)) {
            val title = context.getString(R.string.notify_weather_alert_title)
            val message = ""
            val firstBodyText = context.getString(R.string.notify_weather_alert_first_body)
            val breakline = "\n"
            val secondBodyText = context.getString(R.string.notify_weather_alert_second_body)

            NotificationHelper.createSampleDataNotification(context, title, message, firstBodyText + currentWeather + breakline + secondBodyText, false)
        }
    }
}
