package ru.gb.weather.model

import retrofit2.Callback
import ru.gb.weather.model.room.CityEntity
import ru.gb.weather.model.web.OpenWeatherWebEntity

interface Repository {
    fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>)
    fun getCitiesListFromLocalStorage(): List<CityEntity>
}