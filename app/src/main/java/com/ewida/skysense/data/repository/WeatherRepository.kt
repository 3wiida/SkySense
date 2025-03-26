package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.WeatherDetails
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double
    ): Flow<WeatherDetails>

    fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse>

    fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String,
    ): Task<FetchPlaceResponse>
}