package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.WeatherDetails
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

interface RemoteDataSource {
    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unites: String = "metric",
        lang: String = "en"
    ): WeatherDetails

    fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse>

    fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String,
    ): Task<FetchPlaceResponse>
}