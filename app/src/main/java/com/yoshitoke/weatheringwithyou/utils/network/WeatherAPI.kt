package com.yoshitoke.weatheringwithyou.utils.network

import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.WeatherInfo
import io.reactivex.Observable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val GET = "onecall?exclude=hourly,daily&appid=9c1bdf56a58934c804d690c1d30f7714"

interface WeatherAPI {
    @GET(GET)
    fun getWeatherInfo(@Query("lat") cityLon : String, @Query("lon") cityLat : String) : Observable<WeatherInfo>
}