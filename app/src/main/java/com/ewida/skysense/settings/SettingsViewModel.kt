package com.ewida.skysense.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.Result
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.LocationType
import com.ewida.skysense.util.enums.WeatherUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _settings = MutableStateFlow<Result<AppSettings>>(Result.Loading)
    val settings = _settings.asStateFlow()

    init {
        getAppSettings()
    }

    fun getAppSettings() {
        try {
            _settings.value = Result.Success(data = repo.getAppSettings())
        } catch (ex: Exception) {
            _settings.value = Result.Failure(error = ErrorModel(message = ex.message.toString()))
        }
    }

    fun updateLanguage(language: AppLanguage) {
        repo.saveAppLanguage(language)
    }

    fun updateUnit(unit: WeatherUnit) {
        repo.saveWeatherUnit(unit)
    }

    fun updateLocationType(locationType: LocationType){
        repo.saveLocationType(locationType)
    }

    @Suppress("UNCHECKED_CAST")
    class SettingsViewModelFactory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo = repository) as T
        }
    }
}