package com.ewida.skysense.addalert.components.dialogs

import androidx.compose.runtime.Composable
import com.ewida.skysense.R
import com.ewida.skysense.common.PermissionDialog

@Composable
fun NotificationRationalDialog(
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