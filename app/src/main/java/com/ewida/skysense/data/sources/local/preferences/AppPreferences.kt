package com.ewida.skysense.data.sources.local.preferences

import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.enums.AppLanguages

interface AppPreferences {
    fun getAppSettings(): AppSettings

    fun saveAppLanguage(language: AppLanguages)
}