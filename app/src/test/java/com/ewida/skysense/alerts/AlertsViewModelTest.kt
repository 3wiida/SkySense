package com.ewida.skysense.alerts

import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class AlertsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AlertsViewModel
    private lateinit var repo: WeatherRepository

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        viewModel = AlertsViewModel(repo)
    }

    @Test
    fun `getSavedAlerts should update savedAlertsResult with Success`() = runTest {
        // Arrange
        val fakeAlerts = listOf(
            WeatherAlert(id = "1", lat = 10.0, long = 20.0, timeStamp = 1234),
            WeatherAlert(id = "2", lat = 30.0, long = 40.0, timeStamp = 5678)
        )
        every { repo.getAllWeatherAlerts() } returns flowOf(fakeAlerts)

        // Act
        viewModel.getSavedAlerts()
        advanceUntilIdle()

        // Assert
        val result = viewModel.savedAlertsResult.value
        assertThat(result is Result.Success, `is`(true))
        assertThat((result as Result.Success).data, `is`(fakeAlerts))
    }

    @Test
    fun `deleteAlert should update isDeletedSuccessfully when delete is successful`() = runTest {
        // Arrange
        val alert = WeatherAlert(id = "4", lat = 70.0, long = 80.0, timeStamp = 6789)
        coEvery { repo.deleteWeatherAlert(alert) } returns 1

        // Act
        viewModel.deleteAlert(alert)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.isDeletedSuccessfully.value, `is`(true))
    }



    @Test
    fun `deleteAlert should not update isDeletedSuccessfully when delete fails`() = runTest {
        // Arrange
        val alert = WeatherAlert(id = "4", lat = 70.0, long = 80.0, timeStamp = 6789)
        coEvery { repo.deleteWeatherAlert(alert) } returns 0

        // Act
        viewModel.deleteAlert(alert)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.isDeletedSuccessfully.value, `is`(false))
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}