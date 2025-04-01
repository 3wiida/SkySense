package com.ewida.skysense.placepicker.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.ewida.skysense.R
import com.ewida.skysense.util.LocationUtils
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState

@Composable
fun PickingMap(
    markerState: MarkerState,
    cameraPositionState: CameraPositionState
) {
    var placeName by remember { mutableStateOf("") }
    val context = LocalContext.current

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(mapType = MapType.HYBRID),
        uiSettings = MapUiSettings().copy(zoomControlsEnabled = false),
        onMapClick = { newPosition ->
            markerState.position = newPosition
            placeName = LocationUtils.getLocationAddressLine(
                context = context,
                latitude = markerState.position.latitude,
                longitude = markerState.position.longitude
            )?.subAdminArea ?: context.getString(R.string.unknown_location)
            markerState.showInfoWindow()
        }
    ) {
        MarkerInfoWindowContent(
            state = markerState,
            onClick = {
                markerState.hideInfoWindow()
                false
            },
        ) {
            Text(
                text = placeName,
                fontWeight = FontWeight.Bold
            )
        }
    }
}