package com.ewida.skysense.util

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.ewida.skysense.util.enums.AppLanguages
import java.util.Locale

object LanguageUtils {
    fun changeLanguage(activity: Activity, language: AppLanguages) {
        val languageCode = when (language) {
            AppLanguages.ENGLISH -> "en"
            AppLanguages.ARABIC -> "ar"
        }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setLocaleForApi33(activity, languageCode)
        } else {
            activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        }
        activity.recreate()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setLocaleForApi33(activity: Activity, languageCode: String) {
        val locale = Locale.forLanguageTag(languageCode)
        val localeManager = activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        localeManager.applicationLocales = LocaleList(locale)
    }
}