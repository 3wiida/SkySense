package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.WeatherDetails

interface RemoteDataSource {
    suspend fun getWeatherDetails(latitude: Double, longitude: Double): WeatherDetails
}