package com.ewida.skysense.data.model

data class SingleHourForecast(
    val main: Main,
    val weather: List<Weather>,
)