package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.LocalDataSource
import com.ewida.skysense.data.sources.remote.RemoteDataSource
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepositoryImpl private constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {

    override fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unites: String,
        lang: String
    ): Flow<WeatherDetails> = flow {
        val cachedDetails = localDataSource.getWeatherDetails(latitude, longitude)

        cachedDetails?.let {
            emit(it)
        }

        try {
            val updatedDetails = remoteDataSource.getWeatherDetails(
                latitude,
                longitude,
                unites,
                lang
            )
            localDataSource.saveWeatherDetails(updatedDetails)
            emit(updatedDetails)
        } catch (e: Exception) {
            if (cachedDetails == null) throw e
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

    override suspend fun getRemoteWeatherDetails(
        latitude: Double,
        longitude: Double,
        lang: String
    ): WeatherDetails {
        return remoteDataSource.getWeatherDetails(latitude, longitude,lang)
    }

    override fun getSavedPlacesDetails(): Flow<List<WeatherDetails>> {
        return localDataSource.getSavedPlacesDetails()
    }

    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        return remoteDataSource.fetchPlacePredictions(placesClient, query)
    }

    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        return remoteDataSource.fetchPlaceDetails(placesClient, placeId)
    }

    override suspend fun saveWeatherAlert(weatherAlert: WeatherAlert) {
        localDataSource.saveWeatherAlert(weatherAlert)
    }

    override suspend fun deleteWeatherAlert(weatherAlert: WeatherAlert) {
        localDataSource.deleteWeatherAlert(weatherAlert)
    }

    override suspend fun deleteAlertByID(alertID: String) {
        localDataSource.deleteAlertByID(alertID)
    }

    override fun getAllWeatherAlerts(): Flow<List<WeatherAlert>> {
        return localDataSource.getAllWeatherAlerts()
    }

    override fun getAppSettings(): AppSettings {
        return localDataSource.getAppSettings()
    }

    override fun saveAppLanguage(language: AppLanguage) {
        localDataSource.saveAppLanguage(language)
    }

    override fun saveWeatherUnit(unit: WeatherUnit) {
        localDataSource.saveWeatherUnit(unit)
    }

    companion object {
        private var instance: WeatherRepositoryImpl? = null
        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource
        ): WeatherRepositoryImpl {
            return instance ?: synchronized(lock = this) {
                val repo = WeatherRepositoryImpl(localDataSource, remoteDataSource)
                instance = repo
                repo
            }
        }
    }
}