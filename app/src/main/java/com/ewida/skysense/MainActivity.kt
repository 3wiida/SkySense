package com.ewida.skysense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ewida.skysense.navigation.AppNavHost
import com.ewida.skysense.navigation.Screens
import com.ewida.skysense.ui.theme.SkySenseTheme
import com.ewida.skysense.util.hasLocationPermission
import com.ewida.skysense.workers.AlertWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isKeepSplashScreen = MutableStateFlow(true)

        installSplashScreen().setKeepOnScreenCondition {
            isKeepSplashScreen.value
        }

        val startDestination = if (hasLocationPermission()) {
            if (intent != null) {
                val alertLat = intent.getDoubleExtra(AlertWorker.ALERT_LAT_KEY, 0.0)
                val alertLong = intent.getDoubleExtra(AlertWorker.ALERT_LONG_KEY, 0.0)
                if (alertLat == 0.0 && alertLong == 0.0) {
                    Screens.WeatherDetails(null, null)
                } else {
                    Screens.WeatherDetails(alertLat, alertLong)
                }
            }else{
                Screens.WeatherDetails(null, null)
            }
        } else {
            Screens.Permissions
        }

        setContent {
            SkySenseTheme {
                AppNavHost(
                    startDestination = startDestination
                )
            }
        }

        lifecycleScope.launch {
            delay(2000)
            isKeepSplashScreen.value = false
        }
    }
}