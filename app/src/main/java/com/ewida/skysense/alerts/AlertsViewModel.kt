package com.ewida.skysense.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.ewida.skysense.util.Result
import kotlinx.coroutines.Dispatchers

class AlertsViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _savedAlertsResult = MutableStateFlow<Result<List<WeatherAlert>>>(Result.Loading)
    val savedAlertsResult = _savedAlertsResult.asStateFlow()

    private val _isDeletedSuccessfully = MutableStateFlow(false)
    val isDeletedSuccessfully = _isDeletedSuccessfully.asStateFlow()

    init {
        getSavedAlerts()
    }

    fun getSavedAlerts() {
        viewModelScope.launch {
            try {
                repo.getAllWeatherAlerts().collect { alerts ->
                    _savedAlertsResult.emit(Result.Success(alerts))
                }
            } catch (e: Exception) {
                _savedAlertsResult.emit(
                    Result.Failure(
                        error = ErrorModel(
                            message = e.localizedMessage ?: ""
                        )
                    )
                )
            }
        }
    }

    fun deleteAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            _isDeletedSuccessfully.emit(false)
            val result = repo.deleteWeatherAlert(alert)
            _isDeletedSuccessfully.emit(result >= 1)
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