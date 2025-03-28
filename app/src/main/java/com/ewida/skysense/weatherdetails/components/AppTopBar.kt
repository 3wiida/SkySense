package com.ewida.skysense.weatherdetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ewida.skysense.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onSavedPlacesClicked: () -> Unit,
    onAlertsClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {},
        actions = {
            IconButton(
                onClick = onSavedPlacesClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_saved),
                    contentDescription = "save"
                )
            }

            IconButton(
                onClick = { isMenuExpanded = !isMenuExpanded }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
                )
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.alerts))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        onAlertsClicked()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.settings))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        onSettingsClicked()
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = Color.White
        )
    )
}