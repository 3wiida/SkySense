package com.ewida.skysense.data.sources.local.db.typeconverters

import androidx.room.TypeConverter
import com.ewida.skysense.data.model.Current
import com.ewida.skysense.data.model.Daily
import com.ewida.skysense.data.model.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrent(value: Current): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurrent(value: String): Current {
        return gson.fromJson(value, Current::class.java)
    }

    @TypeConverter
    fun fromHourlyList(value: List<Hourly>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHourlyList(value: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDailyList(value: List<Daily>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDailyList(value: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(value, type)
    }
}