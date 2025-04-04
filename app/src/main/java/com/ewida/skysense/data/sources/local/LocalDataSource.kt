package com.ewida.skysense.data.sources.local

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {
    suspend fun saveWeatherDetails(details: WeatherDetails)
    suspend fun getWeatherDetails(latitude: Double, longitude: Double): WeatherDetails?
    fun getSavedPlacesDetails(): Flow<List<WeatherDetails>>
    suspend fun deleteSavedPlace(place: WeatherDetails)

    suspend fun saveWeatherAlert(weatherAlert: WeatherAlert)
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