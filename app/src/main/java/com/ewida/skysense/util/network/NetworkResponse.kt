package com.ewida.skysense.util.network

import com.ewida.skysense.data.model.ErrorModel

sealed class NetworkResponse<out T> {
    data object Loading : NetworkResponse<Nothing>()
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Failure(val error: ErrorModel) : NetworkResponse<Nothing>()
}