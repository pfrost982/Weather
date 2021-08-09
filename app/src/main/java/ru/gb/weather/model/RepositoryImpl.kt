package ru.gb.weather.model

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RepositoryImpl : Repository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: OpenWeatherWebService = retrofit.create(OpenWeatherWebService::class.java)

    override fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>) {
        service.getCurrentWeather(city.cityName).enqueue(callback)
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
