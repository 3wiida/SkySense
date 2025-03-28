package com.ewida.skysense.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri

object PermissionUtils {
    fun onPermissionResult(
        permission: String,
        isPermissionGranted: Boolean,
        activity: Activity?,
        onGranted: () -> Unit,
        onShowRational: () -> Unit,
        onPermanentlyRefused: () -> Unit
    ) {
        if (isPermissionGranted) {
            onGranted()
        } else {
            if (activity?.shouldShowRequestPermissionRationale(permission) == true) {
                onShowRational()
            } else {
                onPermanentlyRefused()
            }
        }
    }

    fun openAppSettings(activity: Activity?) {
        activity?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", it.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            it.startActivity(intent)
        }
    }

    fun openDisplayOverAppsSettings(activity: Activity?) {
        activity?.startActivity(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:${activity.packageName}".toUri()
            )
        )
    }
}