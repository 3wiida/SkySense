package com.ewida.skysense.weatherdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.ErrorModel
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.network.NetworkResponse
import com.ewida.skysense.util.network.getError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherDetailsViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _detailsResponse = MutableStateFlow<NetworkResponse<WeatherDetails>>(NetworkResponse.Loading)
    val detailsResponse = _detailsResponse.asStateFlow()

    fun getWeatherDetails(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repo.getWeatherDetails(latitude, longitude)
                .catch { throwable ->
                    emitError(throwable)
                }
                .collect { details ->
                    _detailsResponse.value = NetworkResponse.Success(details)
                }
        }
    }

    private suspend fun emitError(throwable: Throwable) {
        _detailsResponse.emit(
            NetworkResponse.Failure(
                error = when (throwable) {
                    is HttpException -> throwable.getError()
                    is IOException -> ErrorModel(message = "No Internet Connection Available")
                    else -> ErrorModel(message = throwable.message ?: "Unknown error")
                }
            )
        )
    }


    @Suppress("UNCHECKED_CAST")
    class WeatherDetailsViewModelFactory(private val repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherDetailsViewModel(repo = repo) as T
        }
    }
}