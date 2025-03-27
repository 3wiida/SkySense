package com.ewida.skysense.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Permissions : Screens()

    @Serializable
    data class WeatherDetails(
        val placeLat: Double?,
        val placeLong: Double?
    ) : Screens()

    @Serializable
    data class SavedPlaces(
        val currentLocationLat: Double,
        val currentLocationLong: Double
    ) : Screens()

    @Serializable
    data class PlacePicker(
        val initialLat: Double,
        val initialLong: Double
    ) : Screens()
}