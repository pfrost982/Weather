package ru.gb.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.weather.model.City
import ru.gb.weather.model.Repository
import ru.gb.weather.model.RepositoryImpl
import ru.gb.weather.model.Weather
import ru.gb.weather.model.web.OpenWeatherWebEntity

class MainViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repositoryImpl: Repository = RepositoryImpl()
    fun getLiveData() = liveDataToObserve

    fun addCity(city: City) {
        repositoryImpl.addCityToLocalStorage(city)
        getCitiesListFromLocalSource()
    }

    fun deleteCity(city: City) {
        repositoryImpl.deleteCityFromLocalStorage(city)
        getCitiesListFromLocalSource()
    }

    fun getCitiesListFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(
                AppState.SuccessList(
                    repositoryImpl.getCitiesListFromLocalStorage()
                )
            )
        }.start()
    }

    fun getWeatherFromRemoteSource(city: City, newCity: Boolean = false) {
        liveDataToObserve.value = AppState.Loading
        repositoryImpl.getWeatherFromServer(city,
            object : Callback<OpenWeatherWebEntity> {
                override fun onResponse(
                    call: Call<OpenWeatherWebEntity>,
                    response: Response<OpenWeatherWebEntity>
                ) {
                    if (response.isSuccessful) {
                        val openWeatherWebEntity = response.body()
                        if (openWeatherWebEntity != null) {
                            liveDataToObserve.postValue(
                                AppState.SuccessWeather(
                                    weatherParse(openWeatherWebEntity), newCity
                                )
                            )
                        }
                    } else {
                        liveDataToObserve.postValue(AppState.Error(Throwable("Response is not Successful")))
                    }
                }

                override fun onFailure(call: Call<OpenWeatherWebEntity>, t: Throwable) {
                    liveDataToObserve.postValue(AppState.Error(t))
                }
            }
        )
    }

    private fun weatherParse(openWeatherWebEntity: OpenWeatherWebEntity): Weather {
        return Weather(
            City(
                openWeatherWebEntity.id,
                openWeatherWebEntity.name,
                openWeatherWebEntity.coord.lat,
                openWeatherWebEntity.coord.lon
            ),
            openWeatherWebEntity.main.temp,
            openWeatherWebEntity.main.feelsLike,
            openWeatherWebEntity.weather[0].description,
            openWeatherWebEntity.weather[0].icon
        )
    }
}

