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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.util.enums.WeatherUnit

@Composable
fun UnitSection(
    unit: WeatherUnit,
    onUnitChanged: (WeatherUnit) -> Unit
) {
    var currentUnit by remember { mutableStateOf(unit) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.units),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = unit == WeatherUnit.METRIC,
                    onClick = {
                        if (unit != WeatherUnit.METRIC) {
                            currentUnit = WeatherUnit.METRIC
                            onUnitChanged(WeatherUnit.METRIC)
                        }
                    }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.metric)
                    )
                    Text(
                        text = "Temperature by Celsius and Wind Speed by meter/sec",
                        color = Color(0xFFA9A9A9)
                    )
                }
            }

            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = unit == WeatherUnit.STANDARD,
                    onClick = {
                        if (unit != WeatherUnit.STANDARD) {
                            currentUnit = WeatherUnit.STANDARD
                            onUnitChanged(WeatherUnit.STANDARD)
                        }
                    }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.standard)
                    )
                    Text(
                        text = "Temperature by Kelvin and Wind Speed by meter/sec",
                        color = Color(0xFFA9A9A9)
                    )
                }
            }

            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = unit == WeatherUnit.IMPERIAL,
                    onClick = {
                        if (unit != WeatherUnit.IMPERIAL) {
                            currentUnit = WeatherUnit.IMPERIAL
                            onUnitChanged(WeatherUnit.IMPERIAL)
                        }
                    }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = ""
                    )
                    Text(
                        text = "Temperature by Fahrenheit and Wind Speed by miles/hour",
                        color = Color(0xFFA9A9A9)
                    )
                }
            }
        }
    }
}