package com.ewida.skysense.weatherdetails

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.location.LocationUtils
import com.ewida.skysense.util.network.NetworkResponse
import com.ewida.skysense.util.roundTo
import com.ewida.skysense.weatherdetails.components.AppTopBar
import com.ewida.skysense.weatherdetails.components.CurrentWeatherSection
import com.ewida.skysense.weatherdetails.components.DailyForecastBottomSheet
import com.ewida.skysense.weatherdetails.components.HourlyTempSection
import com.ewida.skysense.weatherdetails.components.WeatherDetailsFailureState
import com.ewida.skysense.weatherdetails.components.WeatherDetailsLoadingState
import com.ewida.skysense.weatherdetails.components.WeatherInsightsSection

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherDetailsScreen(
    viewModel: WeatherDetailsViewModel
) {
    val detailsResponse = viewModel.detailsResponse.collectAsStateWithLifecycle()
    val cachedData = viewModel.cachedData.collectAsStateWithLifecycle()

    var isDaysForecastBottomSheetShown by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var addressLine by remember { mutableStateOf("") }

    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                onSavedPlacesClicked = {},
                onAlertsClicked = {},
                onSettingsClicked = {}
            )
        }
    ) {

        WeatherDetailsScreenContent(
            detailsResponse = detailsResponse.value,
            cachedData = cachedData.value,
            addressLine = addressLine,
            onShowDaysForecast = {
                isDaysForecastBottomSheetShown = !isDaysForecastBottomSheetShown
            },
            onFailureRetryClicked = {
                currentLocation?.let {
                    viewModel.getWeatherDetails(
                        latitude = it.latitude.roundTo(2),
                        longitude = it.longitude.roundTo(2)
                    )
                }
            }
        )

    }


    cachedData.value?.let {
        if (isDaysForecastBottomSheetShown) {
            DailyForecastBottomSheet(
                forecast = it.daily,
                onDismiss = {
                    isDaysForecastBottomSheetShown = !isDaysForecastBottomSheetShown
                }
            )
        }
    }

    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                LocationUtils.getCurrentLocation(
                    context = context,
                    onLocationAvailable = { location ->
                        Log.d("```TAG```", "lat: ${location.latitude.roundTo(2)}")
                        Log.d("```TAG```", "lon: ${location.longitude.roundTo(2)}")
                        viewModel.getWeatherDetails(
                            latitude = location.latitude.roundTo(2),
                            longitude = location.longitude.roundTo(2)
                        )
                        LocationUtils.getLocationAddressLine(
                            context = context,
                            location = location,
                            onAddressAvailable = { addressLine = it }
                        )
                        currentLocation = location
                    }
                )
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
    modifier: Modifier = Modifier,
    detailsResponse: NetworkResponse<WeatherDetails>,
    cachedData: WeatherDetails?,
    addressLine: String,
    onShowDaysForecast: () -> Unit,
    onFailureRetryClicked: () -> Unit
) {
    when (detailsResponse) {
        is NetworkResponse.Loading -> {
            WeatherDetailsLoadingState()
        }

        is NetworkResponse.Failure -> {
            if (cachedData == null) {
                WeatherDetailsFailureState(
                    cause = detailsResponse.error.message,
                    onRetryClicked = onFailureRetryClicked
                )
            } else {
                WeatherDetailsUI(
                    modifier = modifier,
                    details = cachedData,
                    addressLine = addressLine,
                    onShowDaysForecast = onShowDaysForecast
                )
            }
        }

        is NetworkResponse.Success<*> -> {
            detailsResponse.data?.let {
                WeatherDetailsUI(
                    modifier = modifier,
                    details = it as WeatherDetails,
                    addressLine = addressLine,
                    onShowDaysForecast = onShowDaysForecast
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailsUI(
    modifier: Modifier = Modifier,
    details: WeatherDetails,
    addressLine: String,
    onShowDaysForecast: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CurrentWeatherSection(
            current = details.current,
            addressLine = addressLine
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

        WeatherInsightsSection(
            insights = details.current
        )
    }
}





