package ru.gb.weather

import android.app.Application
import androidx.room.Room
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.weather.model.room.CitiesDao
import ru.gb.weather.model.room.CitiesDataBase
import ru.gb.weather.model.web.OpenWeatherWebService

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: CitiesDataBase? = null
        private var service: OpenWeatherWebService? = null

        fun getCitiesDao(): CitiesDao {
            if (db == null) {
                synchronized(CitiesDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                            db = Room.databaseBuilder(
                                appInstance!!.applicationContext,
                                CitiesDataBase::class.java,
                                "cities-db"
                            )
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return db!!.getDao()
        }

        fun getServiceOpenWeather(): OpenWeatherWebService {
            if (service == null) {
                synchronized(OpenWeatherWebService::class.java) {
                    if (service == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        val retrofit: Retrofit = Retrofit.Builder()
                            .baseUrl("https://api.openweathermap.org/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        service = retrofit.create(OpenWeatherWebService::class.java)
                    }
                }
            }
            return service!!
        }
    }
}