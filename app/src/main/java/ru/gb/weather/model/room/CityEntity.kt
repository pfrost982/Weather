package ru.gb.weather.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "city_lat") val lat: Double,
    @ColumnInfo(name = "city_lon") val lon: Double
)
