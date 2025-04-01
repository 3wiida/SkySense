package com.ewida.skysense.settings.components

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R
import com.ewida.skysense.util.enums.AppLanguage

@Composable
fun LanguageSettings(
    language: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(language) }

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
                        if(selectedLanguage != AppLanguage.ENGLISH){
                            selectedLanguage = AppLanguage.ENGLISH
                            onLanguageChanged(AppLanguage.ENGLISH)
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
                        if(selectedLanguage != AppLanguage.ARABIC){
                            selectedLanguage = AppLanguage.ARABIC
                            onLanguageChanged(AppLanguage.ARABIC)
                        }
                    }
                )
                Text(text = stringResource(R.string.arabic))
            }
        }
    }
}