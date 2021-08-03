package ru.gb.weather.model

import org.json.JSONObject
import ru.gb.weather.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(city: City): Weather {
        val uri =
            "https://api.openweathermap.org/data//2.5/weather?q=${city.cityName}&units=metric&lang=ru&APPID=${BuildConfig.WEATHER_API_KEY}"
        val connection =
            URL(uri).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 10_000
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readLines().joinToString()
        connection.disconnect()
        return parse(response)
    }

    private fun parse(response: String): Weather {
        val jsonResponse = JSONObject(response)
        val jsonWeather = jsonResponse.getJSONArray("weather").getJSONObject(0)
        val jsonCoordinates = jsonResponse.getJSONObject("coord")
        val jsonMain = jsonResponse.getJSONObject("main")
        val city = City(
            jsonResponse.getString("name"),
            jsonCoordinates.getDouble("lat"),
            jsonCoordinates.getDouble("lon")
        )
        return Weather(
            city,
            jsonMain.getDouble("temp"),
            jsonMain.getDouble("feels_like"),
            jsonWeather.getString("description")
        )
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
