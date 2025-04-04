package com.ewida.skysense.data.sources.local

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(
    private val localWeatherDetails: MutableList<WeatherDetails> = mutableListOf(),
    private val weatherAlerts: MutableList<WeatherAlert> = mutableListOf(),
    private var appSettings: AppSettings = AppSettings(),
    private var mapLocation: LatLng? = null
) : LocalDataSource {
    override suspend fun saveWeatherDetails(details: WeatherDetails) {
        localWeatherDetails.add(details)
    }

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double
    ): WeatherDetails? {
        return localWeatherDetails.firstOrNull { item -> item.lat == latitude && item.lon == longitude }
    }

    override fun getSavedPlacesDetails(): Flow<List<WeatherDetails>> {
        return flow {
            emit(localWeatherDetails)
        }
    }

    override suspend fun deleteSavedPlace(place: WeatherDetails) {
        localWeatherDetails.remove(place)
    }

    override suspend fun saveWeatherAlert(weatherAlert: WeatherAlert) {
        weatherAlerts.add(weatherAlert)
    }

    override suspend fun deleteWeatherAlert(weatherAlert: WeatherAlert): Int {
        weatherAlerts.remove(weatherAlert)
        return 1
    }

    override suspend fun deleteAlertByID(alertID: String) {
        weatherAlerts.removeIf { item -> item.id == alertID }
    }

    override fun getAllWeatherAlerts(): Flow<List<WeatherAlert>> {
        return flow {
            emit(weatherAlerts)
        }
    }

    override fun getAppSettings(): AppSettings {
        return appSettings
    }

    override fun saveAppLanguage(language: AppLanguage) {
        appSettings = appSettings.copy(language = language)
    }

    override fun saveWeatherUnit(unit: WeatherUnit) {
        appSettings = appSettings.copy(unit = unit)
    }

    override fun saveLocationType(type: LocationType) {
        appSettings = appSettings.copy(locationType = type)
    }

    override fun saveMapLocation(place: LatLng) {
        mapLocation = place
    }

    override fun getMapLocation(): Pair<Double, Double> {
        return mapLocation?.let {
            Pair(it.latitude, it.longitude)
        } ?: throw NullPointerException("Map location is not set")
    }
}