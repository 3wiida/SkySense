package com.ewida.skysense.data.sources.local

import com.ewida.skysense.data.model.WeatherDetails
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {
    suspend fun saveWeatherDetails(details: WeatherDetails)
    suspend fun getWeatherDetails(latitude: Double, longitude: Double): WeatherDetails?
    fun getSavedPlacesDetails(): Flow<List<WeatherDetails>>
}