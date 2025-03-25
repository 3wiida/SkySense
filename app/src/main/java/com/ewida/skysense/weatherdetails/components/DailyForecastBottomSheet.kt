package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Daily
import com.ewida.skysense.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyForecastBottomSheet(
    forecast: List<Daily>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string._7_days_forecast),
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = forecast) { dayForecast ->
                    SingleDayForecast(dayForecast = dayForecast)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SingleDayForecast(dayForecast: Daily) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if(dayForecast.isYesterday()){
                stringResource(R.string.yesterday)
            } else if(dayForecast.isToday()) {
                stringResource(R.string.today)
            }else if(dayForecast.isTomorrow()){
                stringResource(R.string.tomorrow)
            }else{
                dayForecast.getDayOfWeek()
            },
            fontWeight = if(dayForecast.isToday()) FontWeight.Bold else FontWeight.Normal
        )

        Text(
            text = dayForecast.getDayMonth(),
            fontWeight = if(dayForecast.isToday()) FontWeight.Bold else FontWeight.Normal
        )

        GlideImage(
            modifier = Modifier.size(56.dp),
            model = "${Constants.WEATHER_ICON_BASE_URL}${dayForecast.weather.first().icon}.png",
            contentDescription = null
        )

        Text(
            text = "H: ${dayForecast.temp.max.toInt()}\u00B0C",
            fontWeight = if(dayForecast.isToday()) FontWeight.Bold else FontWeight.Normal
        )

        Text(
            text = "L: ${dayForecast.temp.min.toInt()}\u00B0C",
            fontWeight = if(dayForecast.isToday()) FontWeight.Bold else FontWeight.Normal
        )
    }
}