package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Current
import com.ewida.skysense.util.Constants
import java.time.Instant

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeatherSection(
    current: Current,
    addressLine: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = getCurrentWeatherGradientColors(
                        stateId = current.weather.first().id,
                        isNight = Instant.now().epochSecond >= current.sunset
                    )
                )
            )
            .padding(top = 82.dp)
    ) {
        if (current.weather.first().id >= 500 && current.weather.first().id < 800) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(
                    if (current.weather.first().id < 600)
                        R.drawable.rain_effect
                    else
                        R.drawable.snow_effect
                ),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            if (getWeatherIcon(current.weather.first().icon) != -1) {
                Image(
                    modifier = Modifier.size(140.dp),
                    painter = painterResource(getWeatherIcon(current.weather.first().icon)),
                    contentDescription = "Drawable Image"
                )
            } else {
                GlideImage(
                    modifier = Modifier.size(140.dp),
                    model = "${Constants.WEATHER_ICON_BASE_URL}${current.weather.first().icon}.png",
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "${current.temp.toInt()}\u00B0C",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 56.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = current.weather.first().main,
                    style = MaterialTheme.typography.titleMedium,
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
                        text = addressLine,
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
                    .height(180.dp)
                    .offset(y = (20).dp),
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}


private fun getWeatherIcon(iconId: String): Int {
    return when (iconId) {
        "01d" -> R.drawable.sun
        "01n" -> R.drawable.moon
        "03d", "03n", "04d", "04n" -> R.drawable.clouds
        "09d", "09n", "10d", "10n" -> R.drawable.rain
        "13d", "13n" -> R.drawable.snow
        else -> -1
    }
}

private fun getCurrentWeatherGradientColors(
    stateId: Int,
    isNight: Boolean
): List<Color> {
    return when {
        (stateId == 800 && !isNight) -> listOf(Color(0xFF81A1DB), Color(0xFFC7E3FF))
        (stateId == 800) -> listOf(Color(0xFF472B97), Color(0xFF8C8ADE))
        (stateId >= 500 && stateId < 600) -> listOf(Color(0xFF081225), Color(0xFF183655))
        (stateId >= 600 && stateId < 700) -> listOf(Color(0xFF8FAFE9), Color(0xFF637281))
        else -> listOf(Color(0xFF81A1DB), Color(0xFFC7E3FF))
    }
}