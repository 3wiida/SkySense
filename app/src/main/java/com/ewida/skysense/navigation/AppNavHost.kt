package com.ewida.skysense.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.permissionrequest.PermissionRequestScreen
import com.ewida.skysense.splash.SplashScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsViewModel
import kotlin.reflect.KClass

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController(),
    startDestination: Screens
) {
    val context = LocalContext.current
    val repository = WeatherRepositoryImpl(
        localDataSource = LocalDataSourceImpl(dao = WeatherDatabase.getInstance(context).getDao()),
        remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
    )

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable<Screens.Permissions> {
            PermissionRequestScreen(
                onNavigateToWeatherDetails = {
                    navHostController.navigate(Screens.WeatherDetails) {
                        popUpTo(Screens.Permissions) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.WeatherDetails> {
            WeatherDetailsScreen(
                viewModel = viewModel(
                    factory = WeatherDetailsViewModel.WeatherDetailsViewModelFactory(repo = repository)
                )
            )
        }
    }
}