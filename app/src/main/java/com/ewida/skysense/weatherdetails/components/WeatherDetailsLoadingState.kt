package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.common.shimmerBrush

@Composable
fun WeatherDetailsLoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(brush = shimmerBrush())
                .padding(top = 82.dp)
        )

        Text(
            modifier = Modifier
                .padding(bottom = 8.dp, top = 16.dp)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string._24_hours_forecast),
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = 7) {
                Spacer(
                    modifier = Modifier
                        .width(75.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = shimmerBrush())
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = shimmerBrush())
        )

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
            items(count = 6) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush = shimmerBrush())
                )
            }
        }
    }
}