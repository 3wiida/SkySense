package com.ewida.skysense.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ewida.skysense.permissionrequest.PermissionRequestScreen
import com.ewida.skysense.splash.SplashScreen

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Splash
    ){
        composable<Screens.Splash> {
            SplashScreen(
                onNavigateToPermissions = {
                    navHostController.navigate(Screens.Permissions){
                        popUpTo(Screens.Splash){
                            inclusive = true
                        }
                    }
                },

                onNavigateToWeatherDetails = {

                }
            )
        }

        composable<Screens.Permissions> {
            PermissionRequestScreen(
                onNavigateToWeatherDetails = {

                }
            )
        }
    }
}