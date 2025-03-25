package com.ewida.skysense.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Insight(
    @StringRes val title: Int,
    val state: String,
    @DrawableRes val icon: Int
)
