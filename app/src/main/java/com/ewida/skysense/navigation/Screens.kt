package com.ewida.skysense.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Permissions : Screens()

    @Serializable
    data object WeatherDetails : Screens()
}