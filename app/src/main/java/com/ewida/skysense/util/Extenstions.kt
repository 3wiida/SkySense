package com.ewida.skysense.util

fun Double.roundTo(decimals: Int): Double {
    return "%.${decimals}f".format(this).toDouble()
}