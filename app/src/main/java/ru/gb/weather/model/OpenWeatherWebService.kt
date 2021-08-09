package ru.gb.weather.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.gb.weather.BuildConfig

interface OpenWeatherWebService {
    @GET("data/2.5/weather?units=metric&lang=ru&APPID=${BuildConfig.WEATHER_API_KEY}")
    fun getCurrentWeather(@Query("q") cityName: String): Call<OpenWeatherWebEntity>
}