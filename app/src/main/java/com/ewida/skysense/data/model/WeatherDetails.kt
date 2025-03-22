package com.ewida.skysense.data.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.ewida.skysense.data.sources.local.db.typeconverters.WeatherConverters

@Entity(tableName = "WEATHER_DETAILS", primaryKeys = ["lat","lon"])
@TypeConverters(WeatherConverters::class)
data class WeatherDetails(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)