package com.ewida.skysense.data.model

data class WeatherDetails(
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val weather: List<Weather>,
    val wind: Wind
)