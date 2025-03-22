package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double
    ): Flow<NetworkResponse<WeatherDetails>>
}