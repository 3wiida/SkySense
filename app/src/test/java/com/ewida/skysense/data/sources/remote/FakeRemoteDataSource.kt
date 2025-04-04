package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.WeatherDetails
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class FakeRemoteDataSource(
    private val remoteWeatherDetails: MutableList<WeatherDetails> = mutableListOf()
) : RemoteDataSource {
    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unites: String,
        lang: String
    ): WeatherDetails {
        return remoteWeatherDetails.first { item -> item.lat == latitude && item.lon == longitude }
    }

    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        TODO("Not yet implemented")
    }

    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        TODO("Not yet implemented")
    }
}