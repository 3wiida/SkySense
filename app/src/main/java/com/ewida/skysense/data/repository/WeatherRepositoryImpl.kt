package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.sources.local.LocalDataSource
import com.ewida.skysense.data.sources.remote.RemoteDataSource
import com.ewida.skysense.util.network.NetworkResponse
import com.ewida.skysense.util.network.getError
import kotlinx.coroutines.Dispatchers
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
    ) = flow {
        val localDetails = localDataSource.getWeatherDetails(latitude, longitude)
        emit(NetworkResponse.Loading(cachedData = localDetails))
        try {
            val remoteDetails = remoteDataSource.getWeatherDetails(latitude, longitude)
            localDataSource.saveWeatherDetails(details = remoteDetails)
            emit(NetworkResponse.Success(data = remoteDetails))
        }catch (_: IOException) {
            emit(
                NetworkResponse.Failure(
                    error = ErrorModel(message = "Connection Issue, Try again later")
                )
            )
        } catch (exception: HttpException) {
            emit(
                NetworkResponse.Failure(
                    error = exception.getError()
                )
            )
        } catch (exception: Exception) {
            emit(
                NetworkResponse.Failure(
                    error = ErrorModel(message = exception.message.toString())
                )
            )
        }
    }.flowOn(Dispatchers.IO)

}