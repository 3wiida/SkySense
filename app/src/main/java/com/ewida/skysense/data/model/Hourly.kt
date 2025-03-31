package com.ewida.skysense.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>,
) {
    fun getHourFromUnixTimeStamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("hh a", Locale.getDefault())
        return formatter.format(Date(timestamp * 1000))
    }
}