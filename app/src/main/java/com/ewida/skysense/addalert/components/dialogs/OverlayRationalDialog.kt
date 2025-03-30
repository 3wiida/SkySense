package com.ewida.skysense.addalert.components.dialogs

import androidx.compose.runtime.Composable
import com.ewida.skysense.R
import com.ewida.skysense.common.PermissionDialog

@Composable
fun OverlayRationalDialog(
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