package com.ewida.skysense.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ewida.skysense.R
import com.ewida.skysense.common.ScreenHeader
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.settings.components.LanguageSettings
import com.ewida.skysense.settings.components.UnitSection
import com.ewida.skysense.util.LanguageUtils
import com.ewida.skysense.util.Result
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.WeatherUnit

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit
) {
    val activity = LocalActivity.current
    val settingsResult = viewModel.settings.collectAsStateWithLifecycle()

    when (val result = settingsResult.value) {
        is Result.Loading -> {}
        is Result.Success<AppSettings> -> {
            SettingsScreenContent(
                settings = result.data,
                onLanguageChanged = { language ->
                    viewModel.updateLanguage(language)
                    activity?.let {
                        LanguageUtils.changeLanguage(
                            activity = it,
                            language = language
                        )
                    }
                },
                onUnitChanged = viewModel::updateUnit,
                onBackClicked = onNavigateUp
            )
        }

        is Result.Failure -> {}
    }

}

@Composable
private fun SettingsScreenContent(
    settings: AppSettings,
    onBackClicked: () -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
    onUnitChanged: (WeatherUnit) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            title = stringResource(R.string.settings),
            onBackClicked = onBackClicked
        )

        LanguageSettings(
            language = settings.language,
            onLanguageChanged = onLanguageChanged
        )

        UnitSection(
            unit = settings.unit,
            onUnitChanged = onUnitChanged
        )
    }
}