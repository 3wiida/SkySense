package com.ewida.skysense

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.media.AudioAttributes
import androidx.core.net.toUri
import com.ewida.skysense.util.Constants
import com.google.android.libraries.places.api.Places

class SkySenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyA5rMOFNsoUcLMTq3YiLny0A0mG48lmO3c")
        createAlertsNotificationChannel()
    }

    private fun createAlertsNotificationChannel() {
        val soundUri =
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/raw/notification_sound".toUri()

        val soundAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channelName = getString(R.string.weather_alerts)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val alertsChannel = NotificationChannel(
            Constants.ALERTS_NOTIFICATION_CHANNEL_ID,
            channelName,
            importance
        ).apply {
            setSound(soundUri, soundAttributes)
            enableLights(true)
            enableVibration(true)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(alertsChannel)
    }
}