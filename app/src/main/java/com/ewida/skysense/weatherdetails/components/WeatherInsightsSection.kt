package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ewida.skysense.data.model.Current
import java.util.Date

@Composable
fun WeatherInsights(insights: Current) {
    Column {
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
                FeelsLikeCard(feelsLikeTemp = insights.feels_like.toInt())
            }

            item {
                VisibilityCard(visibilityDistance = insights.visibility)
            }

            item {
                WindCard(
                    windSpeed = insights.wind_speed,
                    windDeg = insights.wind_deg
                )
            }
        }
    }
}