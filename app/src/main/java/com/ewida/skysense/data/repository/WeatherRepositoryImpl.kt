package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.LocalDataSource
import com.ewida.skysense.data.sources.remote.RemoteDataSource
import com.ewida.skysense.util.network.NetworkResponse
import com.ewida.skysense.util.network.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class WeatherRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double
    ): Flow<WeatherDetails> = flow {
        val cachedDetails = localDataSource.getWeatherDetails(
            latitude = latitude,
            longitude = longitude
        )
        emit(cachedDetails)
        val updatedDetails = remoteDataSource.getWeatherDetails(
            latitude = latitude,
            longitude = longitude
        )
        localDataSource.saveWeatherDetails(details = updatedDetails)
        emit(updatedDetails)

    }.flowOn(Dispatchers.IO)

}