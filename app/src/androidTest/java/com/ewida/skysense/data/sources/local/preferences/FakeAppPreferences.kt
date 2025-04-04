package com.ewida.skysense.data.sources.local.preferences

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng

class FakeAppPreferences(
    private var settings: AppSettings = AppSettings(),
    private var mapPlace: LatLng? = null
) : AppPreferences {
    override fun getAppSettings(): AppSettings {
        return settings
    }

    override fun saveAppLanguage(language: AppLanguage) {
        settings = settings.copy(language = language)
    }

    override fun saveWeatherUnit(unit: WeatherUnit) {
        settings = settings.copy(unit = unit)
    }

    override fun saveLocationType(type: LocationType) {
        settings = settings.copy(locationType = type)
    }

    override fun saveMapLocation(place: LatLng) {
        mapPlace = place
    }

    override fun getMapLocation(): Pair<Double, Double> {
        return mapPlace?.let {
            Pair(it.latitude, it.longitude)
        } ?: throw NullPointerException("Map location is not set")
    }
}