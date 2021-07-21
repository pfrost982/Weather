package ru.gb.weather.model

interface Repository {
    fun getWeatherFromServer(city: City): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}