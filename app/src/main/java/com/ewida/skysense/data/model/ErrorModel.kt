package com.ewida.skysense.data.model

data class ErrorModel(
    val cod: Int = 0,
    val message: String = "",
    val parameters: List<String>? = null
)