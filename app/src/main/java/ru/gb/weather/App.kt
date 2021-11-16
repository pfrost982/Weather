package ru.gb.weather

import android.app.Application
import androidx.room.Room
import ru.gb.weather.model.room.CitiesDao
import ru.gb.weather.model.room.CitiesDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: App? = null
        private var db: CitiesDataBase? = null

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
    }
}