package com.ewida.skysense.saved.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.enums.WeatherUnit
import com.ewida.skysense.util.formatTemperature
import com.ewida.skysense.util.LocationUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedPlaceItem(
    modifier: Modifier = Modifier,
    placeDetails: WeatherDetails,
    unit: WeatherUnit,
    onPlaceClicked: (WeatherDetails) -> Unit,
    onDeletePlace: (WeatherDetails) -> Unit
) {
    val context = LocalContext.current
    var placeName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        placeName = LocationUtils.getLocationAddressLine(
            context = context,
            latitude = placeDetails.lat,
            longitude = placeDetails.lon
        )?.subAdminArea ?: context.getString(R.string.unknown_location)

    }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF081225),
                            Color(0xFF183655)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .combinedClickable(
                    onClick = { onPlaceClicked(placeDetails) },
                    onLongClick = { expanded = true }
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = placeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = placeDetails.current.weather.first().description,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = placeDetails.current.temp.toInt().formatTemperature(unit),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "${
                        placeDetails.daily.first().temp.min.toInt().formatTemperature(unit)
                    } / ${placeDetails.daily.first().temp.max.toInt().formatTemperature(unit)}",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.delete)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        tint = Color.Red,
                        contentDescription = null
                    )
                },
                onClick = {
                    onDeletePlace(placeDetails)
                    expanded = false
                }
            )
        }
    }
}