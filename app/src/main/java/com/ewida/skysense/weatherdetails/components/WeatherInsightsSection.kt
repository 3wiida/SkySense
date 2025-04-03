package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Current
import com.ewida.skysense.util.enums.WeatherUnit

@Composable
fun WeatherInsightsSection(
    insights: Current,
    unit: WeatherUnit
) {
    Column {
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.weather_insights),
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
                .padding(horizontal = 24.dp),
            columns = GridCells.Fixed(2),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                UVCard(uvi = insights.uvi.toInt())
            }

            item {
                HumidityCard(humidityPercent = insights.humidity)
            }

            item {
                PressureCard(pressure = insights.pressure)
            }

            item {
                FeelsLikeCard(
                    feelsLikeTemp = insights.feels_like.toInt(),
                    unit = unit
                )
            }

            item {
                VisibilityCard(visibilityDistance = insights.visibility)
            }

            item {
                WindCard(
                    windSpeed = insights.wind_speed,
                    windDeg = insights.wind_deg,
                    unit = unit
                )
            }
        }
    }
}