package com.ewida

import android.app.Application
import com.google.android.libraries.places.api.Places

class SkySenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyA5rMOFNsoUcLMTq3YiLny0A0mG48lmO3c")
    }
}