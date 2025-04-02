package com.ewida.skysense.settings.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.util.enums.LocationType

@Composable
fun LocationSettings(
    preferredLocation: LocationType,
    onLocationTypeChanged: (LocationType) -> Unit
) {
    var selectedLocation by rememberSaveable { mutableStateOf(preferredLocation) }

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.location),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedLocation == LocationType.GPS,
                    onClick = {
                        if (selectedLocation != LocationType.GPS) {
                            selectedLocation = LocationType.GPS
                            onLocationTypeChanged(LocationType.GPS)
                        }
                    }
                )
                Text(text = stringResource(R.string.gps))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedLocation == LocationType.MAP,
                    onClick = {
                        selectedLocation = LocationType.MAP
                        onLocationTypeChanged(LocationType.MAP)
                    }
                )
                Text(text = stringResource(R.string.map))
            }
        }
    }
}