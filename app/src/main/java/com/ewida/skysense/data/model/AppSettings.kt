package com.ewida.skysense.data.model

import com.ewida.skysense.util.enums.AppLanguages

data class AppSettings(
    val language: AppLanguages = AppLanguages.ENGLISH
)
