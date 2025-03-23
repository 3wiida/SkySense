package com.ewida.skysense.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.permissionrequest.PermissionRequestScreen
import com.ewida.skysense.splash.SplashScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsViewModel

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController(),
    repo: WeatherRepository
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Splash
    ) {
        composable<Screens.Splash> {
            SplashScreen(
                onNavigateToPermissions = {
                    navHostController.navigate(Screens.Permissions) {
                        popUpTo(Screens.Splash) {
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
                    navHostController.navigate(Screens.WeatherDetails) {
                        popUpTo(Screens.Splash) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.WeatherDetails> {
            WeatherDetailsScreen(
                viewModel = viewModel(
                    factory = WeatherDetailsViewModel.WeatherDetailsViewModelFactory(repo = repo)
                )
            )
        }
    }
}