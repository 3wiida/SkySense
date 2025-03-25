package com.ewida.skysense.weatherdetails

import com.ewida.skysense.data.model.WeatherDetails

data class WeatherDetailsUIState(
    val isLoading: Boolean = false,
    val details: WeatherDetails? = null,
    val error:String= ""
)
