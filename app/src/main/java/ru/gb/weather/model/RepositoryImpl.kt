package ru.gb.weather.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(city: City) = Weather(City(city.city, 100.1010, 99.9999))
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
