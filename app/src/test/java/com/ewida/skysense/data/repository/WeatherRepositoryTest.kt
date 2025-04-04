package com.ewida.skysense.data.repository

import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.FakeLocalDataSource
import com.ewida.skysense.data.sources.remote.FakeRemoteDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {
    private val remoteWeatherDetails = listOf(
        WeatherDetails(lat = 10.0, lon = 20.0, timezone = "Cairo", timezone_offset = 10),
        WeatherDetails(lat = 30.0, lon = 40.0, timezone = "NewYork", timezone_offset = 10),
        WeatherDetails(lat = 50.0, lon = 60.0, timezone = "Paris", timezone_offset = 10),
    )

    private val localWeatherDetails = listOf(
        WeatherDetails(lat = 10.0, lon = 20.0, timezone = "Cairo", timezone_offset = 10)
    )

    private val weatherAlerts = listOf(
        WeatherAlert(id = "1", lat = 10.0, long = 20.0, timeStamp = 123456, alertType = "Popup")
    )

    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        fakeLocalDataSource = FakeLocalDataSource(
            localWeatherDetails = localWeatherDetails.toMutableList(),
            weatherAlerts = weatherAlerts.toMutableList(),
        )

        fakeRemoteDataSource = FakeRemoteDataSource(
            remoteWeatherDetails = remoteWeatherDetails.toMutableList()
        )

        repository = WeatherRepositoryImpl.getInstance(
            localDataSource = fakeLocalDataSource,
            remoteDataSource = fakeRemoteDataSource
        )
    }

    @Test
    fun getRemoteWeatherDetails_givenLatLng_returnsDetailsOfThisPlace() = runTest {
        //Arrange
        val lat = 10.0
        val long = 20.0

        //Act
        val result = repository.getRemoteWeatherDetails(
            latitude = lat,
            longitude = long,
            lang = "en",
            unites = "metric"
        )

        //Assert
        assertThat(result.lat, `is`(remoteWeatherDetails.first().lat))
        assertThat(result.lon, `is`(remoteWeatherDetails.first().lon))
        assertThat(result.timezone, `is`(remoteWeatherDetails.first().timezone))
        assertThat(result.timezone_offset, `is`(remoteWeatherDetails.first().timezone_offset))
    }

    @Test
    fun getSavedPlacesDetails_countOfSavedIs1AndSavedItemIsTheSameAsLocalItem() = runTest {
        //Act
        val savedPlaces = repository.getSavedPlacesDetails().first()

        assertEquals(1, savedPlaces.size)
        assertThat(savedPlaces.first().lat, `is`(localWeatherDetails.first().lat))
        assertThat(savedPlaces.first().lon, `is`(localWeatherDetails.first().lon))
        assertThat(savedPlaces.first().timezone, `is`(localWeatherDetails.first().timezone))
        assertThat(savedPlaces.first().timezone_offset, `is`(localWeatherDetails.first().timezone_offset))
    }
}