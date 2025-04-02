package com.ewida.skysense.placepicker

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.placepicker.components.PickingMap
import com.ewida.skysense.placepicker.components.PlacesSearchBar
import com.ewida.skysense.placepicker.components.SaveButton
import com.ewida.skysense.util.ActionResult
import com.ewida.skysense.util.LocationUtils
import com.ewida.skysense.util.enums.SourceScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun PlacePickerScreen(
    viewModel: PlacePickerViewModel,
    initialLat: Double,
    initialLong: Double,
    source: SourceScreen,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var markerState = remember { MarkerState(position = LatLng(initialLat, initialLong)) }
    val cameraPositionState = rememberCameraPositionState {
        scope.launch {
            animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(initialLat, initialLong),
                    10f
                )
            )
        }
    }

    val predictions = viewModel.searchResults.collectAsStateWithLifecycle()
    val selectedPredictionLatLng = viewModel.predictedPlaceLatLng.collectAsStateWithLifecycle()
    val savePlaceState = viewModel.isPlaceSaved.collectAsStateWithLifecycle()

    PlacePickerScreenContent(
        markerState = markerState,
        savePlaceState = savePlaceState.value,
        cameraPositionState = cameraPositionState,
        predictions = predictions.value,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onPredictionSelected = viewModel::onPredictionSelected,
        onSaveClicked = {
            viewModel.onSaveClicked(
                place = markerState.position,
                sourceScreen = source
            )
        }
    )

    LaunchedEffect(key1 = selectedPredictionLatLng.value) {
        selectedPredictionLatLng.value?.let {
            markerState.position = it
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 10f))
        }
    }

    LaunchedEffect(key1 = savePlaceState.value) {
        when (savePlaceState.value) {
            ActionResult.FAILED -> {
                Toast.makeText(context, R.string.save_place_error, Toast.LENGTH_SHORT).show()
            }

            ActionResult.COMPLETED -> {
                Toast.makeText(context, R.string.save_place_success, Toast.LENGTH_SHORT).show()
                onNavigateUp()
            }

            else -> {}
        }
    }
}

@Composable
private fun PlacePickerScreenContent(
    markerState: MarkerState,
    savePlaceState: ActionResult,
    cameraPositionState: CameraPositionState,
    predictions: List<AutocompletePrediction>,
    onSearchQueryChanged: (String) -> Unit,
    onPredictionSelected: (String) -> Unit,
    onSaveClicked: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PickingMap(
            markerState = markerState,
            cameraPositionState = cameraPositionState
        )

        PlacesSearchBar(
            modifier = Modifier
                .padding(vertical = 56.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onQueryChanged = onSearchQueryChanged,
            predictions = predictions,
            onPlaceSelected = onPredictionSelected
        )

        SaveButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            savePlaceState = savePlaceState,
            onSaveClicked = onSaveClicked
        )
    }
}