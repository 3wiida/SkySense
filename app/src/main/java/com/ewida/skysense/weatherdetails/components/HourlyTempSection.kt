package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Hourly
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.formatTemperature
import com.ewida.skysense.util.formatToDefaultLocale

@Composable
fun HourlyTempSection(
    hourlyTemps: List<Hourly>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.hours_forecast, 24.formatToDefaultLocale()),
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = hourlyTemps) { hourlyItem ->
                HourlySingleItem(hourlyItem = hourlyItem)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun HourlySingleItem(
    hourlyItem: Hourly
) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourlyItem.getHourFromUnixTimeStamp(hourlyItem.dt)
        )

        GlideImage(
            modifier = Modifier.size(56.dp),
            model = "${Constants.WEATHER_ICON_BASE_URL}${hourlyItem.weather.first().icon}.png",
            contentDescription = null
        )

        Text(
            text = hourlyItem.temp.toInt().formatTemperature("C"),
        )
    }
}