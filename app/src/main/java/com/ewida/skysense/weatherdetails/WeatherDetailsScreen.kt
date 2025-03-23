package com.ewida.skysense.weatherdetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Current
import com.ewida.skysense.data.model.Hourly
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.network.NetworkResponse

@Composable
fun WeatherDetailsScreen(
    viewModel: WeatherDetailsViewModel
) {
    val detailsResponse = viewModel.detailsResponse.collectAsStateWithLifecycle()
    val owner = LocalLifecycleOwner.current

    WeatherDetailsScreenContent(
        detailsResponse = detailsResponse.value
    )

    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getWeatherDetails(33.44, -94.04)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun WeatherDetailsScreenContent(
    detailsResponse: NetworkResponse<WeatherDetails>
) {
    when (detailsResponse) {
        is NetworkResponse.Loading -> {
            Log.d("```TAG```", "WeatherDetailsScreenContent: Loading")
        }

        is NetworkResponse.Failure -> {
            Log.d(
                "```TAG```",
                "WeatherDetailsScreenContent: Failure => ${detailsResponse.error.message}"
            )
        }

        is NetworkResponse.Success<*> -> {
            Log.d("```TAG```", "WeatherDetailsScreenContent: Success => ${detailsResponse.data}")
            WeatherDetailsUI(details = detailsResponse.data as WeatherDetails)
        }
    }
}

@Composable
private fun LoadingState() {

}

@Composable
private fun WeatherDetailsUI(details: WeatherDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CurrentWeatherSection(current = details.current)
        Spacer(Modifier.height(16.dp))
        HourlyTempSection(hourlyTemps = details.hourly.take(24))
    }
}

@Composable
private fun CurrentWeatherSection(
    current: Current
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF81A1DB),
                        Color(0xFFC7E3FF)
                    )
                )
            )
            .padding(top = 68.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                modifier = Modifier.size(140.dp),
                painter = painterResource(R.drawable.sun),
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "${current.temp.toInt()}\u00B0C",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Row {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )

                    Text(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(top = 4.dp),
                        text = "Sakhon province, Thailand",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .offset(y = (20).dp),
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

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
            text = stringResource(R.string._24_hours_forecast),
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = {}
        ) {
            Text(
                text = "5 Days Forecast"
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlySingleItem(
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
            text = "${hourlyItem.temp.toInt()}\u00B0C",
        )
    }
}