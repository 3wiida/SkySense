package com.ewida.skysense.util.network

import com.ewida.skysense.data.model.ErrorModel

sealed class NetworkResponse<out T> {
    data class Loading<T>(val cachedData: T? = null) : NetworkResponse<T>()
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Failure(val error: ErrorModel) : NetworkResponse<Nothing>()
}