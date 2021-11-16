package ru.gb.weather.model.room

import androidx.room.*
import ru.gb.weather.model.City

@Dao
interface CitiesDao {
    @Query("SELECT * FROM CityEntity")
    fun getAll(): List<CityEntity>

    @Query("SELECT * FROM CityEntity WHERE city_name LIKE :city")
    fun getCityByName(city: String): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(entity: CityEntity)

    @Update
    fun updateCity(entity: CityEntity)

    @Delete
    fun deleteCity(entity: CityEntity)
}
