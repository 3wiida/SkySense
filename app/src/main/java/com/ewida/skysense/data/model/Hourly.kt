package com.ewida.skysense.data.model

import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>,
){
    fun getHourFromUnixTimeStamp(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("hh a")
        return Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }
}