package ru.gb.weather.viewmodel

import ru.gb.weather.model.City
import ru.gb.weather.model.Weather

sealed class AppState {
    data class SuccessList(val citiesDataList: List<City>) : AppState()
    data class SuccessWeather(val weatherData: Weather, val newCity: Boolean) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
