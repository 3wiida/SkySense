package com.ewida.skysense.addalert.components

import android.Manifest
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ewida.skysense.R
import com.ewida.skysense.common.PermissionDialog
import com.ewida.skysense.util.PermissionUtils
import com.ewida.skysense.util.enums.AlertType
import com.ewida.skysense.util.hasNotificationPermission
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AlertSettings(
    onSetAlertClicked: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var alertType by remember { mutableStateOf(AlertType.NONE) }
    var isShowNotificationRationalDialog by remember { mutableStateOf(false) }
    var isShowSettingsDialog by remember { mutableStateOf(false) }
    var isShowOverlayRationalDialog by remember { mutableStateOf(false) }

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

    val openDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val openTimePicker = {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTime = "$hour:$minute"
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AlertOptions(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            alertType = alertType,
            onPickDateClicked = openDatePicker,
            onPickTimeClicked = openTimePicker,
            onAlertTypeChanged = { type ->
                when (type) {
                    AlertType.NOTIFICATION -> {
                        if (context.hasNotificationPermission()) {
                            alertType = AlertType.NOTIFICATION
                        } else {
                            permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    AlertType.POPUP -> {
                        if (Settings.canDrawOverlays(context)) {
                            alertType = AlertType.POPUP
                        } else {
                            isShowOverlayRationalDialog = true
                        }
                    }

                    AlertType.NONE -> {}
                }
            }
        )

        SetAlertButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onSetAlertClicked = onSetAlertClicked
        )
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
        AppSettingsDialog(
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

@Composable
private fun NotificationRationalDialog(
    onAllowPermissionClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    PermissionDialog(
        image = R.drawable.location_rational_img,
        title = R.string.notification_permission_needed,
        content = R.string.notification_rational_content,
        positiveButtonLabel = R.string.allow,
        onPositiveButtonClicked = onAllowPermissionClicked,
        onDismiss = onDismiss
    )
}

@Composable
private fun AppSettingsDialog(
    onSettingsClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    PermissionDialog(
        image = R.drawable.location_settings_img,
        title = R.string.enable_notification_in_settings,
        content = R.string.notification_settings_content,
        positiveButtonLabel = R.string.settings,
        negativeButtonLabel = R.string.cancel,
        onPositiveButtonClicked = onSettingsClicked,
        onNegativeButtonClicked = onCancelClicked,
        onDismiss = onDismiss
    )
}

@Composable
private fun OverlayRationalDialog(
    onAllowPermissionClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    PermissionDialog(
        image = R.drawable.location_settings_img,
        title = R.string.enable_display_over_other_apps,
        content = R.string.overlay_rational_content,
        positiveButtonLabel = R.string.allow,
        onPositiveButtonClicked = onAllowPermissionClicked,
        onDismiss = onDismiss
    )
}

