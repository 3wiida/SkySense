package com.ewida.skysense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ewida.skysense.splash.SplashScreen
import com.ewida.skysense.ui.theme.SkySenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SkySenseTheme {
                SplashScreen()
            }
        }
    }



}

