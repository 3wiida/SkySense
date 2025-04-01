package com.ewida.skysense.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.ewida.skysense.util.enums.WeatherUnit
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

fun Double.roundTo(decimals: Int): Double {
    return "%.${decimals}f".format(Locale.US, this).toDouble()
}

fun Number.formatToDefaultLocale(): String {
    val numberFormat: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
    return numberFormat.format(this)
}

fun Int.formatTemperature(unit: WeatherUnit): String {
    val locale = Locale.getDefault()
    val numberFormat = NumberFormat.getInstance(locale)

    val convertedTemp = when (unit) {
        /*"C" -> this
        "F" -> (this * 9 / 5 + 32.0).roundToInt()
        "K" -> (this + 273.15).roundToInt()
        else -> this*/
        WeatherUnit.METRIC -> this
        WeatherUnit.STANDARD -> (this + 273.15).roundToInt()
        WeatherUnit.IMPERIAL -> (this * 9 / 5 + 32.0).roundToInt()
    }

    val translatedUnit = when (unit) {
        /*"C" -> if (locale.language == "ar") "°س" else "°C"
        "K" -> if (locale.language == "ar") "°ك" else "°K"
        "F" -> if (locale.language == "ar") "°ف" else "°F"
        else -> unit*/
        WeatherUnit.METRIC -> if (locale.language == "ar") "°س" else "°C"
        WeatherUnit.STANDARD -> if (locale.language == "ar") "°ك" else "°K"
        WeatherUnit.IMPERIAL -> if (locale.language == "ar") "°ف" else "°F"
    }

    return "${numberFormat.format(convertedTemp)} $translatedUnit"
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        true
    } else {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}