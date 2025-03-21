package com.ewida.skysense.util.network

import com.ewida.skysense.data.model.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

fun <T> sendSafeNetworkRequest(call: suspend () -> T) = flow {
    emit(NetworkResponse.Loading())
    try {
        emit(NetworkResponse.Success(data = call.invoke()))
    } catch (_: IOException) {
        emit(
            NetworkResponse.Failure(
                error = ErrorModel(message = "Connection Issue, Try again later")
            )
        )
    } catch (exception: HttpException) {
        emit(
            NetworkResponse.Failure(
                error = getNetworkErrorFromException(exception)
            )
        )
    } catch (exception: Exception) {
        emit(
            NetworkResponse.Failure(
                error = ErrorModel(message = exception.message.toString())
            )
        )
    }
}

private fun getNetworkErrorFromException(exception: HttpException): ErrorModel {
    return try {
        val errorBody = exception.response()?.errorBody()?.string() ?: ""
        val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
        errorModel
    } catch (exception: Exception) {
        ErrorModel()
    }
}