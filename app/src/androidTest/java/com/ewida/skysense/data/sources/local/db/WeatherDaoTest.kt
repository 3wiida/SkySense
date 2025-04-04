package com.ewida.skysense.data.sources.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ewida.skysense.data.model.WeatherDetails
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.getDao()
    }


    @Test
    fun saveWeatherDetails_getItByLatLng() = runTest {
        //Arrange
        val details = WeatherDetails(
            lat = 30.75,
            lon = 40.11,
            timezone = "Africa/Cairo",
            timezone_offset = 10
        )
        dao.saveWeatherDetails(weatherDetails = details)

        //Act
        val result = dao.getLocalWeatherDetails(latitude = details.lat, longitude = details.lon)

        //Assert
        assertNotNull(result)
        assertThat(result.lat, `is`(details.lat))
        assertThat(result.lon, `is`(details.lon))
        assertThat(result.timezone, `is`(details.timezone))
        assertThat(result.timezone_offset, `is`(details.timezone_offset))
    }

    @Test
    fun deleteSavedPlace_placeIsNull() = runTest {
        //Arrange
        val place = WeatherDetails(
            lat = 30.75,
            lon = 40.11,
            timezone = "Africa/Cairo",
            timezone_offset = 10
        )
        dao.saveWeatherDetails(weatherDetails = place)
        dao.deleteSavedPlace(place = place)

        //Act
        val result = dao.getLocalWeatherDetails(latitude = place.lat, longitude = place.lon)

        //Assert
        assertNull(result)
    }

    @After
    fun tearDown() {
        database.close()
    }
}