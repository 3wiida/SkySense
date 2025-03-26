package com.ewida.skysense.data.sources.local

import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.db.WeatherDao

class LocalDataSourceImpl(private val dao: WeatherDao) : LocalDataSource {
    override suspend fun saveWeatherDetails(details: WeatherDetails) {
        dao.saveWeatherDetails(weatherDetails = details)
    }

    override suspend fun getWeatherDetails(latitude: Double, longitude: Double): WeatherDetails? {
        return dao.getLocalWeatherDetails(
            latitude = latitude,
            longitude = longitude
        )
    }
}