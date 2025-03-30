package com.ewida.skysense.addalert.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ewida.skysense.R
import com.ewida.skysense.addalert.components.dialogs.NotificationRationalDialog
import com.ewida.skysense.addalert.components.dialogs.NotificationSettingsDialog
import com.ewida.skysense.addalert.components.dialogs.OverlayRationalDialog
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.util.PermissionUtils
import com.ewida.skysense.util.enums.AlertType
import com.ewida.skysense.util.hasNotificationPermission
import com.ewida.skysense.workers.AlertWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("InlinedApi")
@Composable
fun AlertOptions(
    onSetAlertClicked: (WeatherAlert) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var alertType by remember { mutableStateOf(AlertType.NONE) }

    var isShowNotificationRationalDialog by remember { mutableStateOf(false) }
    var isShowSettingsDialog by remember { mutableStateOf(false) }
    var isShowOverlayRationalDialog by remember { mutableStateOf(false) }

    val openDatePicker = {
        val today = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = today.timeInMillis
        }.show()
    }

    val openTimePicker = {
        val now = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                selectedTime = timeFormat.format(calendar.time)
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    val permissionRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { result ->
        PermissionUtils.onPermissionResult(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            isPermissionGranted = result,
            activity = activity,
            onGranted = {},
            onShowRational = {
                isShowNotificationRationalDialog = true
            },
            onPermanentlyRefused = {
                isShowSettingsDialog = true
            }
        )
    }

    Column {
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
            onClick = openDatePicker
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
            onClick = openTimePicker
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
                        if (context.hasNotificationPermission()) {
                            alertType = AlertType.NOTIFICATION
                        } else {
                            permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
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
                        if (Settings.canDrawOverlays(context)) {
                            alertType = AlertType.POPUP
                        } else {
                            isShowOverlayRationalDialog = true
                        }
                    }
                )
                Text(text = stringResource(R.string.alert_popup))
            }
        }

        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && alertType != AlertType.NONE,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            ),
            onClick = {
                val alert = WeatherAlert(
                    timeStamp = calendar.timeInMillis / 1000,
                    alertType = alertType.name
                )
                onSetAlertClicked(alert)
            }
        ) {
            Text(
                text = stringResource(R.string.set_alert),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }

    AnimatedVisibility(
        visible = isShowNotificationRationalDialog
    ) {
        NotificationRationalDialog(
            onAllowPermissionClicked = {
                permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = {
                isShowNotificationRationalDialog = false
            }
        )
    }

    AnimatedVisibility(
        visible = isShowSettingsDialog
    ) {
        NotificationSettingsDialog(
            onSettingsClicked = {
                PermissionUtils.openAppSettings(activity)
            },
            onCancelClicked = {
                isShowSettingsDialog = false
            },
            onDismiss = {
                isShowSettingsDialog = false
            }
        )
    }

    AnimatedVisibility(
        visible = isShowOverlayRationalDialog
    ) {
        OverlayRationalDialog(
            onAllowPermissionClicked = {
                PermissionUtils.openDisplayOverAppsSettings(activity)
            },
            onDismiss = {
                isShowOverlayRationalDialog = false
            }
        )
    }
}