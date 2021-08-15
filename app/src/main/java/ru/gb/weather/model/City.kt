package ru.gb.weather.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class City(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "city_lat") val lat: Double,
    @ColumnInfo(name = "city_lon") val lon: Double
) : Parcelable