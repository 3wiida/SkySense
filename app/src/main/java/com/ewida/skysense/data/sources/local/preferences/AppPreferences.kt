package com.ewida.skysense.data.sources.local.preferences

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.WeatherUnit

interface AppPreferences {
    fun getAppSettings(): AppSettings

    fun saveAppLanguage(language: AppLanguage)

    fun saveWeatherUnit(unit: WeatherUnit)
}