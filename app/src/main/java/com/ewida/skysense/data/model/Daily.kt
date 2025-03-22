package com.ewida.skysense.data.model

data class Daily(
    val dt: Int,
    val feels_like: FeelsLike,
    val summary: String,
    val temp: Temp,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)