package com.ewida.skysense.data.model

data class Hourly(
    val dt: Int,
    val temp: Double,
    val weather: List<Weather>,
)