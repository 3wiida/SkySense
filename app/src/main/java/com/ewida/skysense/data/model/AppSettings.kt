package com.ewida.skysense.data.model

import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit

data class AppSettings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val unit: WeatherUnit = WeatherUnit.METRIC,
    val locationType: LocationType = LocationType.GPS
)
