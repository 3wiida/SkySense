package com.ewida.skysense.data.sources.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.db.WeatherDao
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.local.preferences.FakeAppPreferences
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LocalDataSourceTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao
    private lateinit var localDataSource: LocalDataSourceImpl

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.getDao()

        localDataSource = LocalDataSourceImpl(
            dao = dao,
            preferences = FakeAppPreferences(
                settings = AppSettings(
                    language = AppLanguage.ENGLISH,
                    unit = WeatherUnit.METRIC,
                    locationType = LocationType.GPS
                ),
                mapPlace = null
            )
        )
    }

    @Test
    fun saveWeatherDetails_getTheSavedDetailsByLatLng() = runTest {
        //Arrange
        val details = WeatherDetails(
            lat = 30.75,
            lon = 40.11,
            timezone = "Africa/Cairo",
            timezone_offset = 10
        )
        localDataSource.saveWeatherDetails(details = details)

        //Act
        val result =
            localDataSource.getWeatherDetails(latitude = details.lat, longitude = details.lon)

        //Assert
        assertNotNull(result)
        assertThat(result!!.lat, `is`(details.lat))
        assertThat(result.lon, `is`(details.lon))
        assertThat(result.timezone, `is`(details.timezone))
        assertThat(result.timezone_offset, `is`(details.timezone_offset))
    }

    @Test
    fun deleteSavedPlace_placeDeletedSuccessfully() = runTest {
        // Arrange
        val details = WeatherDetails(
            lat = 30.75,
            lon = 40.11,
            timezone = "Africa/Cairo",
            timezone_offset = 10
        )
        localDataSource.saveWeatherDetails(details)

        // Act
        localDataSource.deleteSavedPlace(details)
        val result = localDataSource.getWeatherDetails(details.lat, details.lon)

        // Assert
        assertNull(result)
    }


    @Test
    fun saveAppLanguage_AppLanguageArabic_savedLanguageIsArabic() {
        //Arrange
        val language = AppLanguage.ARABIC
        localDataSource.saveAppLanguage(language = language)

        //Act
        val savedLanguage = localDataSource.getAppSettings().language

        //Assert
        assertEquals(AppLanguage.ARABIC, savedLanguage)
    }

    @Test
    fun saveMapLocation_retrievedMapLocationIsTheSame() {
        //Arrange
        val mapLocation = LatLng(30.11, 40.12)

        //Act
        localDataSource.saveMapLocation(place = mapLocation)
        val (lat, long) = localDataSource.getMapLocation()

        assertEquals(30.11, lat)
        assertEquals(40.12,long)
    }

    @Test
    fun getMapLocationWithoutSavingThrowsException() {
        //Act & Assert
        val exception = assertThrows(NullPointerException::class.java) {
            localDataSource.getMapLocation()
        }

        assertEquals("Map location is not set", exception.message)
    }

    @After
    fun tearDown() {
        database.close()
    }
}