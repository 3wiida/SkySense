package com.ewida.skysense.addalert

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.addalert.components.AddAlertHeader
import com.ewida.skysense.addalert.components.AlertLocationSection
import com.ewida.skysense.addalert.components.AlertSettings
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddAlertScreen(
    currentLocationLat: Double,
    currentLocationLong: Double,
    onNavigateUp: () -> Unit
) {
    val markerState = remember {
        MarkerState(
            position = LatLng(
                currentLocationLat,
                currentLocationLong
            )
        )
    }

    AddAlertScreenContent(
        markerState = markerState,
        onBackClicked = onNavigateUp
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun AddAlertScreenContent(
    markerState: MarkerState,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddAlertHeader(
            onBackClicked = onBackClicked
        )

        AlertLocationSection(
            markerState = markerState
        )

        AlertSettings(
            onSetAlertClicked = {}
        )
    }
}