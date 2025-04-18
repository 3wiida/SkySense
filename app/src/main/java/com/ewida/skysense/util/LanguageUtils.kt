package com.ewida.skysense.util

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.ewida.skysense.util.enums.AppLanguage
import java.util.Locale

object LanguageUtils {
    fun changeLanguage(activity: Activity, language: AppLanguage) {
        val languageCode = when (language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC -> "ar"
            AppLanguage.SAME_AS_DEVICE -> getDeviceLanguageCode(activity)
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
        val locale = if (languageCode == "device") {
            LocaleList.getAdjustedDefault().get(0) ?: Locale.getDefault()
        } else {
            Locale.forLanguageTag(languageCode)
        }

        val localeManager = activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        localeManager.applicationLocales = if (languageCode == "device") {
            LocaleList.getEmptyLocaleList()
        } else {
            LocaleList(locale)
        }
    }

    fun getDeviceLanguageCode(activity: Activity?): String {
        return activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val localeManager =
                    activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
                localeManager.systemLocales.get(0)?.language ?: Locale.getDefault().language
            } else {
                Configuration(activity.resources.configuration).locale.language
            }
        } ?: "en"
    }
}