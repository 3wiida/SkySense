package com.ewida.skysense.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Daily(
    val dt: Int,
    val feels_like: FeelsLike,
    val summary: String,
    val temp: Temp,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
) {
    fun isYesterday(): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = dt.toLong() * 1000
        val day = calendar.get(Calendar.DAY_OF_YEAR)

        return (today - 1) == day
    }

    fun isToday(): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = dt.toLong() * 1000
        val day = calendar.get(Calendar.DAY_OF_YEAR)

        return today == day
    }

    fun isTomorrow(): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = dt.toLong() * 1000
        val day = calendar.get(Calendar.DAY_OF_YEAR)

        return (today + 1) == day
    }

    fun getDayOfWeek(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date(dt.toLong() * 1000))
    }

    fun getDayMonth(): String {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        return sdf.format(Date(dt.toLong() * 1000))
    }
}