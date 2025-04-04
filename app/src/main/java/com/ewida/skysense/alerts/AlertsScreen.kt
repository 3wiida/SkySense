package com.ewida.skysense.alerts

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkManager
import com.ewida.skysense.R
import com.ewida.skysense.alerts.components.AlertDeletionDialog
import com.ewida.skysense.alerts.components.AlertsFailureState
import com.ewida.skysense.alerts.components.AlertsLoadingState
import com.ewida.skysense.alerts.components.AlertsSuccessState
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.util.Result
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel,
    onNavigateToAddAlert: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val savedAlertsResult = viewModel.savedAlertsResult.collectAsStateWithLifecycle()
    val alertDeletionState = viewModel.isDeletedSuccessfully.collectAsStateWithLifecycle()

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
            alertsResult = savedAlertsResult.value,
            onAlertDeleteClicked = { alert ->
                viewModel.deleteAlert(alert)
                WorkManager.getInstance(context).cancelWorkById(UUID.fromString(alert.id))
            }
        )
    }

    LaunchedEffect(key1 = alertDeletionState.value) {
        if (alertDeletionState.value) {
            Toast.makeText(context, R.string.alert_deleted_successfully, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun AlertsScreenContent(
    alertsResult: Result<List<WeatherAlert>>,
    onBackClicked: () -> Unit,
    onAlertDeleteClicked: (WeatherAlert) -> Unit
) {
    when (val result = alertsResult) {
        is Result.Loading -> {
            AlertsLoadingState()
        }

        is Result.Failure -> {
            AlertsFailureState(
                cause = result.error.message
            )
        }

        is Result.Success<List<WeatherAlert>> -> {
            var isShowDeletionDialog by remember { mutableStateOf(false) }
            var alertToDelete by remember { mutableStateOf(WeatherAlert()) }

            AlertsSuccessState(
                alerts = result.data,
                onBackClicked = onBackClicked,
                onAlertDeleteClicked = { alert ->
                    isShowDeletionDialog = true
                    alertToDelete = alert
                }
            )

            AnimatedVisibility(
                visible = isShowDeletionDialog
            ) {
                AlertDeletionDialog(
                    onDeleteClicked = {
                        onAlertDeleteClicked(alertToDelete)
                    },
                    onDismiss = {
                        isShowDeletionDialog = false
                    }
                )
            }
        }
    }
}