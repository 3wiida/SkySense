package com.ewida.skysense.navigation

import com.ewida.skysense.util.enums.SourceScreen
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
        val initialLong: Double,
        val source: SourceScreen
    ) : Screens()

    @Serializable
    data class Alerts(
        val currentLocationLat: Double,
        val currentLocationLong: Double
    ) : Screens()

    @Serializable
    data class AddAlert(
        val currentLocationLat: Double,
        val currentLocationLong: Double
    ) : Screens()

    @Serializable
    data class Settings(
        val currentLocationLat: Double,
        val currentLocationLong: Double
    ) : Screens()
}