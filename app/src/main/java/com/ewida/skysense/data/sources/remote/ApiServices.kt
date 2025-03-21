package com.ewida.skysense.data.sources.remote

import com.ewida.skysense.data.model.HourlyForecast
import com.ewida.skysense.data.model.WeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("/weather")
    suspend fun getCurrentWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): WeatherDetails

    @GET("/forecast")
    suspend fun getHourlyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") hoursCount: Int = 8 //Number of forecasts per day every 3 hours (24 / 3 = 8)
    ): HourlyForecast
}