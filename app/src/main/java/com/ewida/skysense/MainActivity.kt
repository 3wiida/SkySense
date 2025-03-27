package com.ewida.skysense

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.navigation.AppNavHost
import com.ewida.skysense.navigation.Screens
import com.ewida.skysense.ui.theme.SkySenseTheme
import com.ewida.skysense.util.hasLocationPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isKeepSplashScreen = MutableStateFlow(true)

        installSplashScreen().setKeepOnScreenCondition {
            isKeepSplashScreen.value
        }

        val startDestination = if (hasLocationPermission())
            Screens.WeatherDetails(null,null)
        else
            Screens.Permissions

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

