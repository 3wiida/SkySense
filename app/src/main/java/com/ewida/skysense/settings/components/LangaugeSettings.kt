package com.ewida.skysense.settings.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.util.LanguageUtils
import com.ewida.skysense.util.enums.AppLanguage

@Composable
fun LanguageSettings(
    language: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    val activity = LocalActivity.current
    var selectedLanguage by rememberSaveable { mutableStateOf(language) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.language),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.offset(x = (-16).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedLanguage == AppLanguage.ENGLISH,
                    onClick = {
                        if (selectedLanguage != AppLanguage.ENGLISH) {
                            when {
                                (LanguageUtils.getDeviceLanguageCode(activity) == "en" && selectedLanguage == AppLanguage.SAME_AS_DEVICE) -> {
                                    selectedLanguage = AppLanguage.ENGLISH
                                }

                                else -> {
                                    selectedLanguage = AppLanguage.ENGLISH
                                    onLanguageChanged(AppLanguage.ENGLISH)
                                }
                            }
                        }
                    }
                )
                Text(text = stringResource(R.string.english))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedLanguage == AppLanguage.ARABIC,
                    onClick = {
                        if (selectedLanguage != AppLanguage.ARABIC) {
                            when {
                                (LanguageUtils.getDeviceLanguageCode(activity) == "ar" && selectedLanguage == AppLanguage.SAME_AS_DEVICE) -> {
                                    selectedLanguage = AppLanguage.ARABIC
                                }

                                else -> {
                                    selectedLanguage = AppLanguage.ARABIC
                                    onLanguageChanged(AppLanguage.ARABIC)
                                }
                            }
                        }
                    }
                )
                Text(text = stringResource(R.string.arabic))
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = selectedLanguage == AppLanguage.SAME_AS_DEVICE,
                    onClick = {
                        if (selectedLanguage != AppLanguage.SAME_AS_DEVICE) {
                            when {
                                (LanguageUtils.getDeviceLanguageCode(activity) == "en" && selectedLanguage == AppLanguage.ENGLISH) || (LanguageUtils.getDeviceLanguageCode(
                                    activity
                                ) == "ar" && selectedLanguage == AppLanguage.ARABIC) -> {
                                    selectedLanguage = AppLanguage.SAME_AS_DEVICE
                                }

                                else -> {
                                    selectedLanguage = AppLanguage.SAME_AS_DEVICE
                                    onLanguageChanged(AppLanguage.SAME_AS_DEVICE)
                                }
                            }
                        }
                    }
                )
                Text(text = stringResource(R.string.same_as_device))
            }
        }
    }
}