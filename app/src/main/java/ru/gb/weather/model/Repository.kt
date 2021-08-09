package ru.gb.weather.model

import retrofit2.Callback

interface Repository {
    fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>)
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}