package com.ewida.skysense.data.sources.local.preferences

import android.content.SharedPreferences
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.AppLanguage
import androidx.core.content.edit
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.ewida.skysense.util.roundTo
import com.google.android.gms.maps.model.LatLng

class AppPreferencesImpl(private val sharedPreferences: SharedPreferences) : AppPreferences {
    override fun getAppSettings(): AppSettings {
        val language = getLanguage()
        val unit = getUnit()
        val locationType = getLocationType()
        return AppSettings(language = language, unit = unit, locationType = locationType)
    }

    private fun getLanguage(): AppLanguage {
        val language = sharedPreferences.getString(
            Constants.SharedPreferences.LANGUAGE_PREFERENCES_KEY,
            "ENGLISH"
        )

        return when (language) {
            "ARABIC" -> AppLanguage.ARABIC
            else -> AppLanguage.ENGLISH
        }
    }

    private fun getUnit(): WeatherUnit {
        val language = sharedPreferences.getString(
            Constants.SharedPreferences.UNIT_PREFERENCES_KEY,
            "METRIC"
        )

        return when (language) {
            "METRIC" -> WeatherUnit.METRIC
            "STANDARD" -> WeatherUnit.STANDARD
            "IMPERIAL" -> WeatherUnit.IMPERIAL
            else -> WeatherUnit.METRIC
        }
    }

    private fun getLocationType(): LocationType {
        val type = sharedPreferences.getString(
            Constants.SharedPreferences.LOCATION_TYPE_PREFERENCES_KEY,
            "GPS"
        )

        return when (type) {
            "GPS" -> LocationType.GPS
            else -> LocationType.MAP
        }
    }

    override fun saveAppLanguage(language: AppLanguage) {
        sharedPreferences.edit {
            putString(
                Constants.SharedPreferences.LANGUAGE_PREFERENCES_KEY,
                language.name
            )
        }
    }

    override fun saveWeatherUnit(unit: WeatherUnit) {
        sharedPreferences.edit {
            putString(
                Constants.SharedPreferences.UNIT_PREFERENCES_KEY,
                unit.name
            )
        }
    }

    override fun saveLocationType(type: LocationType) {
        sharedPreferences.edit {
            putString(
                Constants.SharedPreferences.LOCATION_TYPE_PREFERENCES_KEY,
                type.name
            )
        }
    }

    override fun saveMapLocation(place: LatLng) {
        sharedPreferences.edit {
            putFloat(
                Constants.SharedPreferences.MAP_LOCATION_LAT_PREFERENCES_KEY,
                place.latitude.roundTo(2).toFloat()
            )

            putFloat(
                Constants.SharedPreferences.MAP_LOCATION_LONG_PREFERENCES_KEY,
                place.longitude.roundTo(2).toFloat()
            )
        }
    }

    override fun getMapLocation(): Pair<Double, Double> {
        val lat = sharedPreferences.getFloat(
            Constants.SharedPreferences.MAP_LOCATION_LAT_PREFERENCES_KEY,
            0.0f
        )

        val long = sharedPreferences.getFloat(
            Constants.SharedPreferences.MAP_LOCATION_LONG_PREFERENCES_KEY,
            0.0f
        )

        return Pair(lat.toDouble().roundTo(2), long.toDouble().roundTo(2))
    }
}