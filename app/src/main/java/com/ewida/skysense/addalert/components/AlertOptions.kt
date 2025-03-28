package com.ewida.skysense.addalert.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.util.enums.AlertType

@Composable
fun AlertOptions(
    modifier: Modifier = Modifier,
    selectedDate: String,
    selectedTime: String,
    alertType: AlertType,
    onPickDateClicked: () -> Unit,
    onPickTimeClicked: () -> Unit,
    onAlertTypeChanged: (AlertType) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.select_alert_date),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF818181)
        )

        Button(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = onPickDateClicked
        ) {
            Text(text = if (selectedDate.isEmpty()) stringResource(R.string.pick_a_date) else selectedDate)
        }

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.select_alert_time),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF818181)
        )

        Button(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = onPickTimeClicked
        ) {
            Text(
                text = if (selectedTime.isEmpty()) stringResource(R.string.pick_a_time) else selectedTime
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.alert_by),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF818181)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = alertType == AlertType.NOTIFICATION,
                    onClick = {
                        onAlertTypeChanged(AlertType.NOTIFICATION)
                    }
                )
                Text(text = stringResource(R.string.notification))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = alertType == AlertType.POPUP,
                    onClick = {
                        onAlertTypeChanged(AlertType.POPUP)
                    }
                )
                Text(text = stringResource(R.string.alert_popup))
            }
        }
    }
}