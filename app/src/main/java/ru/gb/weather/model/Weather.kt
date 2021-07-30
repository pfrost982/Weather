package ru.gb.weather.model

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 25,
    val feelsLike: Int = 25
)

fun getDefaultCity() = City("Барнаул", 53.346785, 83.776856)
