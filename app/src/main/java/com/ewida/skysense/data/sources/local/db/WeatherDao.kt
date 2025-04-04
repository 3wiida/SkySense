package com.ewida.skysense.data.sources.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ewida.skysense.data.model.WeatherAlert
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

    @Delete
    suspend fun deleteSavedPlace(place: WeatherDetails)

    @Insert
    suspend fun saveWeatherAlert(weatherAlert: WeatherAlert)

    @Delete
    suspend fun deleteWeatherAlert(weatherAlert: WeatherAlert): Int

    @Query("DELETE FROM WeatherAlert WHERE id = :alertID")
    suspend fun deleteAlertByID(alertID: String)

    @Query("SELECT * FROM WeatherAlert")
    fun getAllWeatherAlerts(): Flow<List<WeatherAlert>>

}