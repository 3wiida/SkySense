package com.ewida.skysense.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _savedAlerts = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val savedAlerts = _savedAlerts.asStateFlow()

    init {
        getSavedAlerts()
    }

    fun getSavedAlerts() {
        viewModelScope.launch {
            repo.getAllWeatherAlerts().collect { alerts ->
                _savedAlerts.emit(alerts)
            }
        }
    }

    fun deleteAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            repo.deleteWeatherAlert(alert)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class AlertsViewModelFactory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlertsViewModel(repo = repository) as T
        }
    }
}