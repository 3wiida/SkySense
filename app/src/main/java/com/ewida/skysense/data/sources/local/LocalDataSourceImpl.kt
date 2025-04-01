package com.ewida.skysense.data.sources.local

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.db.WeatherDao
import com.ewida.skysense.data.sources.local.preferences.AppPreferences
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.WeatherUnit
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val dao: WeatherDao,
    private val preferences: AppPreferences
) : LocalDataSource {
    override suspend fun saveWeatherDetails(details: WeatherDetails) {
        dao.saveWeatherDetails(weatherDetails = details)
    }

    override suspend fun getWeatherDetails(latitude: Double, longitude: Double): WeatherDetails? {
        return dao.getLocalWeatherDetails(
            latitude = latitude,
            longitude = longitude
        )
    }

    override fun getSavedPlacesDetails(): Flow<List<WeatherDetails>> {
        return dao.getSavedPlacesDetails()
    }

    override suspend fun saveWeatherAlert(weatherAlert: WeatherAlert) {
        dao.saveWeatherAlert(weatherAlert)
    }

    override suspend fun deleteWeatherAlert(weatherAlert: WeatherAlert) {
        dao.deleteWeatherAlert(weatherAlert)
    }

    override suspend fun deleteAlertByID(alertID: String) {
        dao.deleteAlertByID(alertID)
    }

    override fun getAllWeatherAlerts(): Flow<List<WeatherAlert>> {
        return dao.getAllWeatherAlerts()
    }

    override fun getAppSettings(): AppSettings {
        return preferences.getAppSettings()
    }

    override fun saveAppLanguage(language: AppLanguage) {
        preferences.saveAppLanguage(language)
    }

    override fun saveWeatherUnit(unit: WeatherUnit) {
        preferences.saveWeatherUnit(unit)
    }
}