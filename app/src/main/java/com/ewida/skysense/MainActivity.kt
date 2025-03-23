package com.ewida.skysense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.navigation.AppNavHost
import com.ewida.skysense.ui.theme.SkySenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        val repository = WeatherRepositoryImpl(
            localDataSource = LocalDataSourceImpl(dao = WeatherDatabase.getInstance(this).getDao()),
            remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
        )

        setContent {
            SkySenseTheme {
                AppNavHost(repo = repository)
            }
        }
    }
}

