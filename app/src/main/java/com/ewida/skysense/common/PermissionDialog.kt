package com.ewida.skysense.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun PermissionDialog(
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes positiveButtonLabel: Int,
    @StringRes negativeButtonLabel: Int? = null,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit = {},
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            DialogContent(
                image = image,
                title = title,
                content = content,
                positiveButtonLabel = positiveButtonLabel,
                negativeButtonLabel = negativeButtonLabel,
                onPositiveButtonClicked = {
                    onPositiveButtonClicked()
                    onDismiss()
                },
                onNegativeButtonClicked = {
                    onNegativeButtonClicked()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun DialogContent(
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes positiveButtonLabel: Int,
    @StringRes negativeButtonLabel: Int? = null,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(175.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(image),
            contentDescription = null
        )

        Text(
            text = stringResource(title),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp),
            text = stringResource(content),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                TextButton(
                    onClick = onPositiveButtonClicked
                ) {
                    Text(
                        text = stringResource(positiveButtonLabel),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                negativeButtonLabel?.let { label ->
                    TextButton(
                        onClick = onNegativeButtonClicked
                    ) {
                        Text(
                            text = stringResource(label),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}