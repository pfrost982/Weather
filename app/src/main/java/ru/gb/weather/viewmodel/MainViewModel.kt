package ru.gb.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.weather.model.*
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {
    private val repositoryImpl: Repository = RepositoryImpl
    fun getLiveData() = liveDataToObserve
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource(city: City) {
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
                                    weatherParse(openWeatherWebEntity)
                                )
                            )
                        }
                    } else {
                        liveDataToObserve.postValue(AppState.Error(Throwable("Response in not Successful")))
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
                openWeatherWebEntity.name,
                openWeatherWebEntity.coord.lat,
                openWeatherWebEntity.coord.lon
            ),
            openWeatherWebEntity.main.temp,
            openWeatherWebEntity.main.feels_like,
            openWeatherWebEntity.weather[0].description
        )
    }

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.SuccessList(
                    if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                    else repositoryImpl.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}

