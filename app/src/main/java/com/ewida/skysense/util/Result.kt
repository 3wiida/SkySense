package com.ewida.skysense.util

import com.ewida.skysense.data.model.ErrorModel

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: ErrorModel) : Result<Nothing>()
}