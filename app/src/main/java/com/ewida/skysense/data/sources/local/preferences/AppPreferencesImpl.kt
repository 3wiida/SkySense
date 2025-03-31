package com.ewida.skysense.data.sources.local.preferences

import android.content.SharedPreferences
import com.ewida.skysense.data.model.AppSettings
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.AppLanguages
import androidx.core.content.edit

class AppPreferencesImpl(private val sharedPreferences: SharedPreferences) : AppPreferences {
    override fun getAppSettings(): AppSettings {
        val language = getLanguage()
        return AppSettings(language = language)
    }

    private fun getLanguage(): AppLanguages {
        val language = sharedPreferences.getString(
            Constants.SharedPreferences.LANGUAGE_PREFERENCES_KEY,
            "ENGLISH"
        )

        return when (language) {
            "ARABIC" -> AppLanguages.ARABIC
            else -> AppLanguages.ENGLISH
        }
    }

    override fun saveAppLanguage(language: AppLanguages) {
        sharedPreferences.edit {
            putString(
                Constants.SharedPreferences.LANGUAGE_PREFERENCES_KEY,
                language.name
            )
        }
    }
}