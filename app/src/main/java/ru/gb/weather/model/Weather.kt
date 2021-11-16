package ru.gb.weather.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val city: City,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val icon: String
) : Parcelable