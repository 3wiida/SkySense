package com.ewida.skysense.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ewida.skysense.addalert.AddAlertScreen
import com.ewida.skysense.alerts.AlertsScreen
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.permissionrequest.PermissionRequestScreen
import com.ewida.skysense.placepicker.PlacePickerScreen
import com.ewida.skysense.placepicker.PlacePickerViewModel
import com.ewida.skysense.saved.SavedPlacesScreen
import com.ewida.skysense.saved.SavedPlacesViewModel
import com.ewida.skysense.weatherdetails.WeatherDetailsScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsViewModel
import com.ewida.skysense.weatherdetails.WeatherDetailsViewModel.WeatherDetailsViewModelFactory
import com.google.android.libraries.places.api.Places

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController(),
    startDestination: Screens
) {
    val context = LocalContext.current
    val repository = WeatherRepositoryImpl.getInstance(
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
                    navHostController.navigate(Screens.WeatherDetails(null, null)) {
                        popUpTo(Screens.Permissions) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.WeatherDetails> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.WeatherDetails>()
            WeatherDetailsScreen(
                viewModel = viewModel(
                    factory = WeatherDetailsViewModelFactory(repo = repository)
                ),
                locationLat = data.placeLat,
                locationLong = data.placeLong,
                onNavigateToSavedPlaces = { lat, long ->
                    navHostController.navigate(
                        Screens.SavedPlaces(
                            currentLocationLat = lat ?: 0.0,
                            currentLocationLong = long ?: 0.0
                        )
                    )
                },
                onNavigateToAlerts = { lat, long ->
                    navHostController.navigate(
                        Screens.Alerts(
                            currentLocationLat = lat ?: 0.0,
                            currentLocationLong = long ?: 0.0
                        )
                    )

                }
            )
        }

        composable<Screens.SavedPlaces> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.SavedPlaces>()
            SavedPlacesScreen(
                userLocationLatitude = data.currentLocationLat,
                userLocationLongitude = data.currentLocationLong,
                viewModel = viewModel(
                    factory = SavedPlacesViewModel.SavedPlacesViewModelFactory(
                        repository = repository
                    )
                ),
                onNavigateToPlacePicker = {
                    navHostController.navigate(
                        Screens.PlacePicker(
                            initialLat = data.currentLocationLat,
                            initialLong = data.currentLocationLong
                        )
                    )
                },
                onNavigateToPlaceDetails = { placeLat, placeLong ->
                    navHostController.navigate(
                        Screens.WeatherDetails(
                            placeLat = placeLat,
                            placeLong = placeLong
                        )
                    ) {
                        popUpTo<Screens.WeatherDetails> {
                            inclusive = true
                        }
                    }
                },
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screens.PlacePicker> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.PlacePicker>()
            PlacePickerScreen(
                initialLat = data.initialLat,
                initialLong = data.initialLong,
                viewModel = viewModel(
                    factory = PlacePickerViewModel.PlacePickerViewModelFactory(
                        repository = repository,
                        placesClient = Places.createClient(context)
                    )
                ),
                onNavigateUp = { navHostController.navigateUp() }
            )
        }

        composable<Screens.Alerts> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.Alerts>()
            AlertsScreen(
                currentLocationLat = data.currentLocationLat,
                currentLocationLong = data.currentLocationLong,
                onNavigateToAddAlert = {
                    navHostController.navigate(
                        Screens.AddAlert(
                            currentLocationLat = data.currentLocationLat,
                            currentLocationLong = data.currentLocationLong
                        )
                    )
                },
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screens.AddAlert> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.AddAlert>()
            AddAlertScreen(
                currentLocationLat = data.currentLocationLat,
                currentLocationLong = data.currentLocationLong,
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}