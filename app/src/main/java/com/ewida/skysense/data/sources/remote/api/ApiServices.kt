package com.ewida.skysense.data.sources.remote.api

import com.ewida.skysense.data.model.WeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("units") unites: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherDetails
}