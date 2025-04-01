package com.ewida.skysense.saved

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.common.ScreenHeader
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.saved.components.SavedPlaceItem
import com.ewida.skysense.util.enums.WeatherUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SavedPlacesScreen(
    viewModel: SavedPlacesViewModel,
    userLocationLatitude: Double,
    userLocationLongitude: Double,
    onNavigateToPlaceDetails: (Double, Double) -> Unit,
    onNavigateToPlacePicker: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val savedPlaces = viewModel.savedPlaces.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToPlacePicker,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) {
        SavedPlacesScreenContent(
            places = savedPlaces.value,
            unit = viewModel.getUnit(),
            userLocationLatitude = userLocationLatitude,
            userLocationLongitude = userLocationLongitude,
            onPlaceClicked = { details ->
                onNavigateToPlaceDetails(details.lat, details.lon)
            },
            onBackClicked = onNavigateUp
        )
    }
}

@Composable
private fun SavedPlacesScreenContent(
    places: List<WeatherDetails>,
    unit: WeatherUnit,
    userLocationLatitude: Double,
    userLocationLongitude: Double,
    onPlaceClicked: (WeatherDetails) -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            title = stringResource(R.string.saved_places),
            onBackClicked = onBackClicked
        )

        if (places.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val currentLocation = places.firstOrNull { item ->
                    item.lat == userLocationLatitude && item.lon == userLocationLongitude
                }

                currentLocation?.let {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            text = stringResource(R.string.current_location),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF818181)
                        )

                        SavedPlaceItem(
                            placeDetails = it,
                            unit = unit,
                            onPlaceClicked = onPlaceClicked
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            text = stringResource(R.string.added_locations),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF818181)
                        )
                    }
                }

                items(
                    items = places.filter { item ->
                        item.lat != userLocationLongitude && item.lon != userLocationLongitude
                    }
                ) { placeDetails ->
                    SavedPlaceItem(
                        placeDetails = placeDetails,
                        unit = unit,
                        onPlaceClicked = onPlaceClicked
                    )
                }
            }
        }
    }
}