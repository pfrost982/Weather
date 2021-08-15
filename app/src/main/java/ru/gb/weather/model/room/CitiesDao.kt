package ru.gb.weather.model.room

import androidx.room.*
import ru.gb.weather.model.City

@Dao
interface CitiesDao {
    @Query("SELECT * FROM City")
    fun getAll(): List<City>

    @Query("SELECT * FROM City WHERE city_name LIKE :city")
    fun getCityByName(city: String): List<City>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(entity: City)

    @Update
    fun updateCity(entity: City)

    @Delete
    fun deleteCity(entity: City)
}
