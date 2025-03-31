package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.remote.api.ApiServices
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class RemoteDataSourceImpl(
    private val apiServices: ApiServices
) : RemoteDataSource {

    override suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unites: String,
        lang: String
    ): WeatherDetails = apiServices.getWeatherDetails(
        latitude = latitude,
        longitude = longitude,
        unites = unites,
        lang = lang
    )

    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        return placesClient.findAutocompletePredictions(request)
    }

    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        val request = FetchPlaceRequest.newInstance(
            placeId,
            listOf(Place.Field.LAT_LNG)
        )

        return placesClient.fetchPlace(request)
    }
}