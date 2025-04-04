package com.ewida.skysense.addalert

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ewida.skysense.R
import com.ewida.skysense.addalert.components.AlertLocationSection
import com.ewida.skysense.addalert.components.AlertOptions
import com.ewida.skysense.common.ScreenHeader
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.util.roundTo
import com.ewida.skysense.workers.AlertWorker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import java.util.Calendar
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddAlertScreen(
    viewModel: AddAlertViewModel,
    currentLocationLat: Double,
    currentLocationLong: Double,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val markerState = remember {
        MarkerState(
            position = LatLng(
                currentLocationLat,
                currentLocationLong
            )
        )
    }

    var finalAlert by remember { mutableStateOf(WeatherAlert()) }

    AddAlertScreenContent(
        markerState = markerState,
        onBackClicked = onNavigateUp,
        onSetAlertClicked = { alert ->
            finalAlert = alert.copy(
                lat = markerState.position.latitude.roundTo(2),
                long = markerState.position.longitude.roundTo(2)
            )
            val requestID = triggerAlertWorker(context, finalAlert)
            finalAlert = finalAlert.copy(id = requestID)
            viewModel.saveWeatherAlert(finalAlert)
            onNavigateUp()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun AddAlertScreenContent(
    markerState: MarkerState,
    onBackClicked: () -> Unit,
    onSetAlertClicked: (WeatherAlert) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 42.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            title = stringResource(R.string.add_alert),
            onBackClicked = onBackClicked
        )

        AlertLocationSection(
            markerState = markerState
        )

        AlertOptions(
            onSetAlertClicked = onSetAlertClicked
        )
    }
}

private fun triggerAlertWorker(context: Context, alert: WeatherAlert): String {
    val now = Calendar.getInstance().timeInMillis / 1000
    val request = OneTimeWorkRequestBuilder<AlertWorker>().setInputData(
        workDataOf(
            AlertWorker.ALERT_LAT_KEY to alert.lat,
            AlertWorker.ALERT_LONG_KEY to alert.long,
            AlertWorker.ALERT_TYPE_KEY to alert.alertType
        )
    ).apply {
        setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        setInitialDelay(alert.timeStamp - now, TimeUnit.SECONDS)
    }.build()
    WorkManager.getInstance(context).enqueue(request)
    return request.id.toString()
}