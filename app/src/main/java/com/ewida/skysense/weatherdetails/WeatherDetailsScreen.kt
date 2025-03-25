package com.ewida.skysense.weatherdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.network.NetworkResponse
import com.ewida.skysense.weatherdetails.components.CurrentWeatherSection
import com.ewida.skysense.weatherdetails.components.DailyForecastBottomSheet
import com.ewida.skysense.weatherdetails.components.HourlyTempSection
import com.ewida.skysense.weatherdetails.components.WeatherInsights

@Composable
fun WeatherDetailsScreen(
    viewModel: WeatherDetailsViewModel
) {
    val detailsResponse = viewModel.detailsResponse.collectAsStateWithLifecycle()
    val cachedData = viewModel.cachedData.collectAsStateWithLifecycle()
    val isDaysForecastBottomSheetShown = remember { mutableStateOf(false) }
    val owner = LocalLifecycleOwner.current

    WeatherDetailsScreenContent(
        detailsResponse = detailsResponse.value,
        cachedData = cachedData.value,
        onShowDaysForecast = {
            isDaysForecastBottomSheetShown.value = !isDaysForecastBottomSheetShown.value
        }
    )

    cachedData.value?.let {
        if (isDaysForecastBottomSheetShown.value) {
            DailyForecastBottomSheet(
                forecast = it.daily,
                onDismiss = {
                    isDaysForecastBottomSheetShown.value = !isDaysForecastBottomSheetShown.value
                }
            )
        }
    }

    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getWeatherDetails(33.44, -94.04)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }

}

@Composable
private fun WeatherDetailsScreenContent(
    detailsResponse: NetworkResponse<WeatherDetails>,
    cachedData: WeatherDetails?,
    onShowDaysForecast: () -> Unit
) {
    when (detailsResponse) {
        is NetworkResponse.Loading -> {

        }

        is NetworkResponse.Failure -> {
            cachedData?.let {
                WeatherDetailsUI(
                    details = it,
                    onShowDaysForecast = onShowDaysForecast
                )
            }
        }

        is NetworkResponse.Success<*> -> {
            detailsResponse.data?.let {
                WeatherDetailsUI(
                    details = it as WeatherDetails,
                    onShowDaysForecast = onShowDaysForecast
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {

}

@Composable
private fun WeatherDetailsUI(
    details: WeatherDetails,
    onShowDaysForecast: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CurrentWeatherSection(
            current = details.current
        )

        Spacer(Modifier.height(16.dp))

        HourlyTempSection(
            hourlyTemps = details.hourly.take(24)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = onShowDaysForecast
        ) {
            Text(
                text = stringResource(R.string._7_days_forecast)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        WeatherInsights(
            insights = details.current
        )
    }
}





