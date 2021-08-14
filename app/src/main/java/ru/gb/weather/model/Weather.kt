package ru.gb.weather.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Double = 25.0,
    val feelsLike: Double = 25.0,
    val description: String = "...",
    val icon: String = "03d"
) : Parcelable

fun getDefaultCity() = City("Барнаул", 53.346785, 83.776856)

fun getCities() = listOf(
    Weather(City("Барнаул", 55.755826, 37.617299900000035), 1.0, 2.0),
    Weather(City("Москва", 55.755826, 37.617299900000035), 1.0, 2.0),
    Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3.0, 3.0),
    Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5.0, 6.0),
    Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7.0, 8.0),
    Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9.0, 10.0),
    Weather(City("Казань", 55.8304307, 49.06608060000008), 11.0, 12.0),
    Weather(City("Челябинск", 55.1644419, 61.4368432), 13.0, 14.0),
    Weather(City("Омск", 54.9884804, 73.32423610000001), 15.0, 16.0),
    Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), 17.0, 18.0),
    Weather(City("Уфа", 54.7387621, 55.972055400000045), 19.0, 20.0)
).toMutableList()



