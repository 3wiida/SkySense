package com.ewida.skysense.data.sources.local.preferences

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng

interface AppPreferences {
    fun getAppSettings(): AppSettings

    fun saveAppLanguage(language: AppLanguage)

    fun saveWeatherUnit(unit: WeatherUnit)

    fun saveLocationType(type: LocationType)

    fun saveMapLocation(place: LatLng)

    fun getMapLocation(): Pair<Double, Double>
}