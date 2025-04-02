package com.ewida.skysense.addalert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAlertViewModel(private val repo: WeatherRepository) : ViewModel() {

    fun saveWeatherAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveWeatherAlert(alert)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class AddAlertViewModelFactory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddAlertViewModel(repo = repository) as T
        }
    }
}