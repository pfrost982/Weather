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

fun getWorldCities() = listOf(
    Weather(City("Лондон", 51.5085300, -0.1257400), 1.0, 2.0),
    Weather(City("Токио", 35.6895000, 139.6917100), 3.0, 4.0),
    Weather(City("Париж", 48.8534100, 2.3488000), 5.0, 6.0),
    Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7.0, 8.0),
    Weather(City("Рим", 41.9027835, 12.496365500000024), 9.0, 10.0),
    Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11.0, 12.0),
    Weather(City("Стамбул", 41.0082376, 28.97835889999999), 13.0, 14.0),
    Weather(City("Вашингтон", 38.9071923, -77.03687070000001), 15.0, 16.0),
    Weather(City("Киев", 50.4501, 30.523400000000038), 17.0, 18.0),
    Weather(City("Пекин", 39.90419989999999, 116.40739630000007), 19.0, 20.0)
)

fun getRussianCities() = listOf(
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
)


