package com.ewida.skysense.weatherdetails

import android.annotation.SuppressLint
import android.location.Location
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.LocationUtils
import com.ewida.skysense.util.Result
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
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
    viewModel: WeatherDetailsViewModel,
    locationLat: Double?,
    locationLong: Double?,
    onNavigateToSavedPlaces: (Double?, Double?) -> Unit,
    onNavigateToAlerts: (Double?, Double?) -> Unit,
    onNavigateToSettings: (Double?, Double?) -> Unit
) {
    val detailsResponse = viewModel.detailsResponse.collectAsStateWithLifecycle()

    var isDaysForecastBottomSheetShown by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var addressLine by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                onSavedPlacesClicked = {
                    onNavigateToSavedPlaces(
                        currentLocation?.latitude?.roundTo(2),
                        currentLocation?.longitude?.roundTo(2)
                    )
                },
                onAlertsClicked = {
                    onNavigateToAlerts(
                        currentLocation?.latitude?.roundTo(2),
                        currentLocation?.longitude?.roundTo(2)
                    )
                },
                onSettingsClicked = {
                    onNavigateToSettings(
                        currentLocation?.latitude?.roundTo(2),
                        currentLocation?.longitude?.roundTo(2)
                    )
                }
            )
        }
    ) {
        WeatherDetailsScreenContent(
            detailsResponse = detailsResponse.value,
            unit = viewModel.getUnit(),
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



    if (isDaysForecastBottomSheetShown) {
        DailyForecastBottomSheet(
            forecast = (detailsResponse.value as Result.Success).data.daily,
            unit = viewModel.getUnit(),
            onDismiss = {
                isDaysForecastBottomSheetShown = !isDaysForecastBottomSheetShown
            }
        )
    }

    LaunchedEffect(Unit) {
        //When coming from saved item or notification
        locationLat?.let {
            viewModel.getWeatherDetails(
                latitude = locationLat.roundTo(2),
                longitude = locationLong!!.roundTo(2)
            )
            addressLine = LocationUtils.getLocationAddressLine(
                context,
                locationLat,
                locationLong
            )?.subAdminArea ?: context.getString(R.string.unknown_location)
        }

        when (viewModel.getLocationType()) {
            LocationType.GPS -> {
                LocationUtils.getCurrentLocation(
                    context = context,
                    onLocationAvailable = { location ->
                        if (locationLat == null) {
                            viewModel.getWeatherDetails(
                                latitude = location.latitude.roundTo(2),
                                longitude = location.longitude.roundTo(2)
                            )
                            addressLine = LocationUtils.getLocationAddressLine(
                                context,
                                location.latitude,
                                location.longitude
                            )?.subAdminArea ?: context.getString(R.string.unknown_location)
                        }
                        currentLocation = location
                    }
                )
            }

            LocationType.MAP -> {
                val (lat, long) = viewModel.getMapLocation()
                if (locationLat == null){
                    viewModel.getWeatherDetails(lat, long)
                    addressLine = LocationUtils.getLocationAddressLine(
                        context,
                        lat,
                        long
                    )?.subAdminArea ?: context.getString(R.string.unknown_location)
                    currentLocation = Location("").apply {
                        latitude = lat
                        longitude = long
                    }
                }
            }
        }

    }

}

@Composable
private fun WeatherDetailsScreenContent(
    modifier: Modifier = Modifier,
    detailsResponse: Result<WeatherDetails>,
    unit: WeatherUnit,
    addressLine: String,
    onShowDaysForecast: () -> Unit,
    onFailureRetryClicked: () -> Unit
) {
    when (detailsResponse) {
        is Result.Loading -> {
            WeatherDetailsLoadingState()
        }

        is Result.Failure -> {
            WeatherDetailsFailureState(
                cause = detailsResponse.error.message,
                onRetryClicked = onFailureRetryClicked
            )
        }

        is Result.Success<*> -> {
            detailsResponse.data?.let {
                WeatherDetailsUI(
                    modifier = modifier,
                    details = it as WeatherDetails,
                    unit = unit,
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
    unit: WeatherUnit,
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
            unit = unit,
            addressLine = addressLine
        )

        Spacer(Modifier.height(16.dp))

        HourlyTempSection(
            hourlyTemps = details.hourly.take(24),
            unit = unit
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