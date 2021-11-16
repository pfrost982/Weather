package ru.gb.weather.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val cityName: String,
    val lat: Double,
    val lon: Double
) : Parcelable