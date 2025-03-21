package com.ewida.skysense.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Splash : Screens()
    @Serializable
    data object Permissions : Screens()
}