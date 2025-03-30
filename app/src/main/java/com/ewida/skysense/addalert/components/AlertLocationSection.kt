package com.ewida.skysense.addalert.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun AlertLocationSection(
    markerState: MarkerState
) {
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        scope.launch {
            animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(markerState.position.latitude, markerState.position.longitude),
                    10f
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.alarm_location),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF818181)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp)),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings().copy(
                zoomControlsEnabled = false,
                compassEnabled = false
            ),
            onMapClick = { positionLatLng ->
                markerState.position = positionLatLng
            }
        ) {
            Marker(state = markerState)
        }
    }
}