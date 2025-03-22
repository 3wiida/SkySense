package com.ewida.skysense.util.network

import com.ewida.skysense.data.model.ErrorModel
import com.google.gson.Gson
import retrofit2.HttpException

fun HttpException.getError(): ErrorModel {
    return try {
        val errorBody = this.response()?.errorBody()?.string() ?: ""
        val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
        errorModel
    } catch (exception: Exception) {
        ErrorModel()
    }
}