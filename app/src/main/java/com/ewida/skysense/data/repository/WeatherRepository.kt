package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unites: String = "metric",
        lang: String = "en"
    ): Flow<WeatherDetails>

    suspend fun getRemoteWeatherDetails(
        latitude: Double,
        longitude: Double,
        lang: String,
        unites: String = "metric",
    ): WeatherDetails

    fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse>

    fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String,
    ): Task<FetchPlaceResponse>

    fun getSavedPlacesDetails(): Flow<List<WeatherDetails>>

    suspend fun saveWeatherAlert(weatherAlert: WeatherAlert)

    suspend fun deleteSavedPlace(place: WeatherDetails)

    suspend fun deleteWeatherAlert(weatherAlert: WeatherAlert): Int

    suspend fun deleteAlertByID(alertID: String)

    fun getAllWeatherAlerts(): Flow<List<WeatherAlert>>

    fun getAppSettings(): AppSettings

    fun saveAppLanguage(language: AppLanguage)

    fun saveWeatherUnit(unit: WeatherUnit)

    fun saveLocationType(type: LocationType)

    fun saveMapLocation(place: LatLng)

    fun getMapLocation(): Pair<Double, Double>
}