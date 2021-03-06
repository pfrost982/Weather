package ru.gb.weather.model

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.weather.App
import ru.gb.weather.model.room.CityEntity
import ru.gb.weather.model.web.OpenWeatherWebEntity
import ru.gb.weather.model.web.OpenWeatherWebService

class RepositoryImpl : Repository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: OpenWeatherWebService = retrofit.create(OpenWeatherWebService::class.java)

    private var citiesDao = App.getCitiesDao()

    override fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>) {
        service.getCurrentWeather(city.cityName).enqueue(callback)
    }

    override fun getCitiesListFromLocalStorage(): List<CityEntity> =
        citiesDao.getAll()
}
