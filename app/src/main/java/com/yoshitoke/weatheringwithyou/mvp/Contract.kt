package com.yoshitoke.weatheringwithyou.mvp

import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.City
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import com.yoshitoke.weatheringwithyou.mvp.presenter.BasePresenter

interface Contract {
    interface View {

        fun showSpinnerList(data: List<City>)

        fun showWeatherInfo(data: WeatherInfo)

        fun hideProgressBar()
    }

    interface Presenter: BasePresenter {

        fun loadCityList()

        fun switchLocation(listPosition: Int)

    }
}
