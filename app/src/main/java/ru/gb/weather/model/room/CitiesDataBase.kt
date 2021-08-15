package ru.gb.weather.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class CitiesDataBase: RoomDatabase() {
    abstract fun getDao(): CitiesDao
}


