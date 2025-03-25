package com.ewida.skysense.data.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.ewida.skysense.data.sources.local.db.typeconverters.WeatherConverters

@Entity(tableName = "WEATHER_DETAILS", primaryKeys = ["lat", "lon"])
@TypeConverters(WeatherConverters::class)
data class WeatherDetails(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String = "",
    val timezone_offset: Int = 0,
    val current: Current = Current(),
    val hourly: List<Hourly> = listOf<Hourly>(),
    val daily: List<Daily> = listOf<Daily>()
)