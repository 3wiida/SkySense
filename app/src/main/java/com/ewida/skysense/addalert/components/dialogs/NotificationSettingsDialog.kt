package com.ewida.skysense.addalert.components.dialogs

import androidx.compose.runtime.Composable
import com.ewida.skysense.R
import com.ewida.skysense.common.PermissionDialog

@Composable
fun NotificationSettingsDialog(
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