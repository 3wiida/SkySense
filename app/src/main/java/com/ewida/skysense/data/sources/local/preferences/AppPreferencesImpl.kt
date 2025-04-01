package com.ewida.skysense.data.sources.local.preferences

import android.content.SharedPreferences
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.AppLanguage
import androidx.core.content.edit
import com.ewida.skysense.util.enums.WeatherUnit

class AppPreferencesImpl(private val sharedPreferences: SharedPreferences) : AppPreferences {
    override fun getAppSettings(): AppSettings {
        val language = getLanguage()
        val unit = getUnit()
        return AppSettings(language = language, unit = unit)
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
}