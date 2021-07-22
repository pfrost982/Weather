package ru.gb.weather.model

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


private const val APP_KEY = "b58b07d6cf65ae743e1353ded4f0d260"

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(city: City): Weather {
        val uri =
            "https://api.openweathermap.org/data//2.5/weather?q=${city.city},ru&APPID=$APP_KEY"
        val connection =
            URL(uri).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 10_000
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readLines().joinToString()
        return Weather(City(response, 100.1010, 99.9999))
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
