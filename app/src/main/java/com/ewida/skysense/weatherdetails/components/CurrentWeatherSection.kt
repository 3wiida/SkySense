package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.ewida.skysense.R
import com.ewida.skysense.data.model.Current

@Composable
fun CurrentWeatherSection(
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