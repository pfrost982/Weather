package ru.gb.weather.model

import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RepositoryImpl : Repository {
    override fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: OpenWeatherWebService = retrofit.create(OpenWeatherWebService::class.java)
        service.getCurrentWeather(city.cityName).enqueue(callback)
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
