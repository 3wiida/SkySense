package com.ewida.skysense.alerts

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkManager
import com.ewida.skysense.R
import com.ewida.skysense.alerts.components.AlertsEmptyState
import com.ewida.skysense.alerts.components.SingleAlertItem
import com.ewida.skysense.common.ScreenHeader
import com.ewida.skysense.data.model.WeatherAlert
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel,
    onNavigateToAddAlert: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val savedAlerts = viewModel.savedAlerts.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAlert,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) {
        AlertsScreenContent(
            onBackClicked = onNavigateUp,
            alerts = savedAlerts.value,
            onAlarmDeleteClicked = { alert ->
                viewModel.deleteAlert(alert)
                WorkManager.getInstance(context).cancelWorkById(UUID.fromString(alert.id))
            }
        )

        AnimatedVisibility(
            visible = savedAlerts.value.isEmpty()
        ) {
            AlertsEmptyState()
        }
    }
}

@Composable
private fun AlertsScreenContent(
    alerts: List<WeatherAlert>,
    onBackClicked: () -> Unit,
    onAlarmDeleteClicked: (WeatherAlert) -> Unit
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
                    onDeleteClicked = onAlarmDeleteClicked
                )
            }
        }
    }
}