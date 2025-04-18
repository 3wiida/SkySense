package com.ewida.skysense.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ewida.skysense.addalert.AddAlertScreen
import com.ewida.skysense.addalert.AddAlertViewModel
import com.ewida.skysense.alerts.AlertsScreen
import com.ewida.skysense.alerts.AlertsViewModel
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.local.preferences.AppPreferencesImpl
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.permissionrequest.PermissionRequestScreen
import com.ewida.skysense.placepicker.PlacePickerScreen
import com.ewida.skysense.placepicker.PlacePickerViewModel
import com.ewida.skysense.saved.SavedPlacesScreen
import com.ewida.skysense.saved.SavedPlacesViewModel
import com.ewida.skysense.settings.SettingsScreen
import com.ewida.skysense.settings.SettingsViewModel
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.SourceScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsScreen
import com.ewida.skysense.weatherdetails.WeatherDetailsViewModel.WeatherDetailsViewModelFactory
import com.google.android.libraries.places.api.Places

@SuppressLint("NewApi")
@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController(),
    startDestination: Screens
) {
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        }
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
                    factory = WeatherDetailsViewModelFactory(
                        repo = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        )
                    )
                ),
                locationLat = data.placeLat,
                locationLong = data.placeLong,
                isFromNotification = data.isFromNotification,
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
                },
                onNavigateToSettings = { lat, long ->
                    navHostController.navigate(
                        Screens.Settings(
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
                        repository = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        )
                    )
                ),
                onNavigateToPlacePicker = {
                    navHostController.navigate(
                        Screens.PlacePicker(
                            initialLat = data.currentLocationLat,
                            initialLong = data.currentLocationLong,
                            source = SourceScreen.SAVED
                        )
                    )
                },
                onNavigateToPlaceDetails = { placeLat, placeLong ->
                    Log.d("````TAG````", "AppNavHost: $placeLat //// $placeLong")
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
                source = data.source,
                viewModel = viewModel(
                    factory = PlacePickerViewModel.PlacePickerViewModelFactory(
                        repository = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        ),
                        placesClient = Places.createClient(context)
                    )
                ),
                onNavigateUp = { navHostController.navigateUp() }
            )
        }

        composable<Screens.Alerts> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.Alerts>()
            AlertsScreen(
                viewModel = viewModel(
                    factory = AlertsViewModel.AlertsViewModelFactory(
                        repository = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        )
                    )
                ),
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
                viewModel = viewModel(
                    factory = AddAlertViewModel.AddAlertViewModelFactory(
                        repository = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        )
                    )
                ),
                currentLocationLat = data.currentLocationLat,
                currentLocationLong = data.currentLocationLong,
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screens.Settings> { navBackStackEntry ->
            val data = navBackStackEntry.toRoute<Screens.Settings>()
            SettingsScreen(
                viewModel = viewModel(
                    factory = SettingsViewModel.SettingsViewModelFactory(
                        repository = WeatherRepositoryImpl.getInstance(
                            localDataSource = LocalDataSourceImpl(
                                dao = WeatherDatabase.getInstance(context).getDao(),
                                preferences = AppPreferencesImpl(
                                    sharedPreferences = context.getSharedPreferences(
                                        Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                )
                            ),
                            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
                        )
                    )
                ),
                onNavigateToPlacePicker = {
                    navHostController.navigate(
                        Screens.PlacePicker(
                            initialLat = data.currentLocationLat,
                            initialLong = data.currentLocationLong,
                            source = SourceScreen.SETTINGS
                        )
                    )
                },
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}