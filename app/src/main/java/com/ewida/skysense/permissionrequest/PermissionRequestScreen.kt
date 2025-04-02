package com.ewida.skysense.permissionrequest

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ewida.skysense.R
import com.ewida.skysense.common.PermissionDialog
import com.ewida.skysense.ui.theme.SkySenseTheme
import com.ewida.skysense.util.PermissionUtils
import com.ewida.skysense.util.hasLocationPermission


@Composable
fun PermissionRequestScreen(
    onNavigateToWeatherDetails: () -> Unit
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current

    var isShowRationalDialog by remember { mutableStateOf(false) }
    var isShowSettingsDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        PermissionUtils.onPermissionResult(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            isPermissionGranted = result,
            activity = activity,
            onGranted = {
                onNavigateToWeatherDetails()
            },
            onShowRational = {
                isShowRationalDialog = true
            },
            onPermanentlyRefused = {
                isShowSettingsDialog = true
            }
        )
    }

    PermissionRequestScreenContent(
        onAllowClicked = {
            isShowRationalDialog = false
            isShowSettingsDialog = false
            launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    )

    AnimatedVisibility(
        visible = isShowRationalDialog
    ) {
        RationalDialog(
            onAllowPermissionClicked = {
                launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            },
            onDismiss = {
                isShowRationalDialog = false
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

    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && context.hasLocationPermission()) {
                onNavigateToWeatherDetails()
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun PermissionRequestScreenContent(
    onAllowClicked: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 24.dp),
                painter = painterResource(R.drawable.location_permission_img),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.location_permission_primary_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.location_permission_secondary_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter),
            onClick = onAllowClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.allow_permission),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }

}

@Composable
private fun RationalDialog(
    onAllowPermissionClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    PermissionDialog(
        image = R.drawable.notification_permission_img,
        title = R.string.location_rational_title,
        content = R.string.location_rational_content,
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
        title = R.string.location_settings_title,
        content = R.string.location_settings_content,
        positiveButtonLabel = R.string.settings,
        negativeButtonLabel = R.string.cancel,
        onPositiveButtonClicked = onSettingsClicked,
        onNegativeButtonClicked = onCancelClicked,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionScreenPreview() {
    SkySenseTheme {
        PermissionRequestScreenContent(
            onAllowClicked = {}
        )
    }
}
