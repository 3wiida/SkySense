package com.ewida.skysense.alerts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.util.location.LocationUtils

@Composable
fun SingleAlertItem(
    alert: WeatherAlert,
    onDeleteClicked: (WeatherAlert) -> Unit
) {
    val context = LocalContext.current
    var placeName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        placeName = LocationUtils.getLocationAddressLine(
            context = context,
            latitude = alert.lat,
            longitude = alert.long
        )?.subAdminArea ?: context.getString(R.string.unknown_location)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(
                brush = Brush.linearGradient(colors = listOf(Color(0xFF081225), Color(0xFF183655))),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = placeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            IconButton(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(24.dp),
                onClick = { onDeleteClicked(alert) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(18.dp),
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = Color.White
                )

                Text(
                    text = alert.getDate(),
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp),
                        painter = painterResource(R.drawable.ic_time),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Text(
                        text = alert.getTime(),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp),
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Text(
                        text = alert.alertType,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}