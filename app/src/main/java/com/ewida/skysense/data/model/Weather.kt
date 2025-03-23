package com.ewida.skysense.data.model

import java.util.Calendar

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)