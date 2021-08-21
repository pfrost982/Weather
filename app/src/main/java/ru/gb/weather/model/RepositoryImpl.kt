package ru.gb.weather.model

import retrofit2.Callback
import ru.gb.weather.App
import ru.gb.weather.model.web.OpenWeatherWebEntity

class RepositoryImpl : Repository {
    private val serviceOpenWeather = App.getServiceOpenWeather()
    private var citiesDao = App.getCitiesDao()

    override fun getWeatherFromServer(city: City, callback: Callback<OpenWeatherWebEntity>) {
        serviceOpenWeather.getCurrentWeather(city.cityName).enqueue(callback)
    }

    override fun getCitiesListFromLocalStorage(): List<City> =
        citiesDao.getAll()

    override fun addCityToLocalStorage(city: City) {
        citiesDao.deleteCityByName(city.cityName)
        citiesDao.insertCity(city)
    }

    override fun deleteCityFromLocalStorage(city: City) {
        citiesDao.deleteCityByName(city.cityName)
    }
}
