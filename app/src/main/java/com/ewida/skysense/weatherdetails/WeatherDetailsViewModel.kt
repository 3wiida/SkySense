package com.ewida.skysense.weatherdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.Result
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.WeatherUnit
import com.ewida.skysense.util.getError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherDetailsViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _detailsResponse = MutableStateFlow<Result<WeatherDetails>>(Result.Loading)
    val detailsResponse = _detailsResponse.asStateFlow()

    fun getWeatherDetails(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repo.getWeatherDetails(
                latitude = latitude,
                longitude = longitude,
                lang = getLanguage()
            ).catch { throwable ->
                emitError(throwable)
            }.collect { details ->
                _detailsResponse.value = Result.Success(details)
            }
        }
    }

    private suspend fun emitError(throwable: Throwable) {
        _detailsResponse.emit(
            Result.Failure(
                error = when (throwable) {
                    is HttpException -> throwable.getError()
                    is IOException -> ErrorModel(message = "No Internet Connection Available")
                    else -> ErrorModel(message = throwable.message ?: "Unknown error")
                }
            )
        )
    }

    private fun getLanguage(): String {
        return when (repo.getAppSettings().language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC -> "ar"
        }
    }

    fun getUnit(): WeatherUnit {
        return repo.getAppSettings().unit
    }


    @Suppress("UNCHECKED_CAST")
    class WeatherDetailsViewModelFactory(private val repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherDetailsViewModel(repo = repo) as T
        }
    }
}