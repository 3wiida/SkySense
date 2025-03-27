package com.ewida.skysense.data.sources.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ewida.skysense.data.model.WeatherDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeatherDetails(weatherDetails: WeatherDetails)

    @Query("SELECT * FROM WEATHER_DETAILS WHERE lat = :latitude AND lon = :longitude")
    suspend fun getLocalWeatherDetails(latitude: Double, longitude: Double): WeatherDetails

    @Query("SELECT * FROM WEATHER_DETAILS")
    fun getSavedPlacesDetails(): Flow<List<WeatherDetails>>

}