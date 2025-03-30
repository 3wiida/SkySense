package com.ewida.skysense.data.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class WeatherAlert(
    @PrimaryKey
    val id: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val timeStamp: Long = 0,
    val alertType: String = ""
) {
    fun getDate(): String {
        val date = Date(timeStamp * 1000)
        val formatter = SimpleDateFormat("MMM, d yyyy", Locale.US)
        return formatter.format(date)
    }

    fun getTime(): String {
        val date = Date(timeStamp * 1000)
        val formatter = SimpleDateFormat("h:mm a", Locale.US)
        return formatter.format(date)
    }
}
