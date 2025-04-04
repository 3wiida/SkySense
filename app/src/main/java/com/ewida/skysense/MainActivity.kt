package com.ewida.skysense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ewida.skysense.navigation.AppNavHost
import com.ewida.skysense.navigation.Screens
import com.ewida.skysense.ui.theme.SkySenseTheme
import com.ewida.skysense.util.NetworkStateObserver
import com.ewida.skysense.util.hasLocationPermission
import com.ewida.skysense.workers.AlertWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), NetworkStateObserver.NetworkCallbacksListener {

    private var isConnectionBarVisible = mutableStateOf(false)
    private lateinit var networkStateObserver: NetworkStateObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStateObserver = NetworkStateObserver(this, this)
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
                    Screens.WeatherDetails(
                        placeLat = alertLat,
                        placeLong = alertLong,
                        isFromNotification = true
                    )
                }
            } else {
                Screens.WeatherDetails(null, null)
            }
        } else {
            Screens.Permissions
        }

        setContent {
            SkySenseTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost(
                        startDestination = startDestination
                    )

                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        visible = isConnectionBarVisible.value,
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    ) {
                        NoInternetConnectionBar()
                    }
                }
            }
        }

        lifecycleScope.launch {
            delay(2000)
            isKeepSplashScreen.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        networkStateObserver.register()
    }

    override fun onPause() {
        super.onPause()
        networkStateObserver.unregister()
    }

    override fun onConnectionAvailable() {
        isConnectionBarVisible.value = false
    }

    override fun onConnectionUnAvailable() {
        isConnectionBarVisible.value = true
    }

    @Composable
    fun NoInternetConnectionBar() {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_no_internet),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.no_internet_connection_available),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}