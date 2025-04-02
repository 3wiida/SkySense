package com.ewida.skysense.alerts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.common.ScreenHeader
import com.ewida.skysense.data.model.WeatherAlert

@Composable
fun AlertsSuccessState(
    alerts: List<WeatherAlert>,
    onBackClicked: () -> Unit,
    onAlertDeleteClicked: (WeatherAlert) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            title = stringResource(R.string.your_alerts),
            onBackClicked = onBackClicked
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = alerts) { alert ->
                SingleAlertItem(
                    alert = alert,
                    onDeleteClicked = onAlertDeleteClicked
                )
            }
        }
    }

    AnimatedVisibility(
        visible = alerts.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertsEmptyState()
    }
}