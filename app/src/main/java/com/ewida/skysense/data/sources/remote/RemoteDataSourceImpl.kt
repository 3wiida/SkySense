package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.remote.api.ApiServices

class RemoteDataSourceImpl(
    private val apiServices: ApiServices
) : RemoteDataSource {

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double
    ): WeatherDetails = apiServices.getWeatherDetails(latitude = latitude, longitude = longitude)

}