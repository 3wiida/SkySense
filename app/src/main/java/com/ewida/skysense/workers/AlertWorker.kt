package com.ewida.skysense.workers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ewida.skysense.MainActivity
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.AlertType
import com.ewida.skysense.util.LocationUtils
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.local.preferences.AppPreferencesImpl
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import com.ewida.skysense.util.enums.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class AlertWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repo = WeatherRepositoryImpl.getInstance(
        localDataSource = LocalDataSourceImpl(
            dao = WeatherDatabase.getInstance(context).getDao(),
            preferences = AppPreferencesImpl(
                sharedPreferences = context.getSharedPreferences(
                    Constants.SharedPreferences.SETTINGS_PREFERENCES_NAME,
                    Context.MODE_PRIVATE
                )
            )
        ),
        remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
    )

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val lat = inputData.getDouble(ALERT_LAT_KEY, 0.0)
        val long = inputData.getDouble(ALERT_LONG_KEY, 0.0)
        val type = inputData.getString(ALERT_TYPE_KEY)

        val cityName = LocationUtils.getLocationAddressLine(
            context,
            lat,
            long
        )?.subAdminArea ?: context.getString(R.string.unknown_location)

        val weatherDetails = repo.getRemoteWeatherDetails(
            latitude = lat,
            longitude = long,
            lang = getLanguage()
        )

        when (type?.uppercase()) {
            AlertType.NOTIFICATION.name -> {
                notify(cityName, weatherDetails)
            }

            AlertType.POPUP.name -> {
                showSlideDownPopup(cityName, weatherDetails)
            }
        }

        repo.deleteAlertByID(workerParams.id.toString())
        return Result.Success()
    }

    @SuppressLint("MissingPermission")
    private fun notify(cityName: String, details: WeatherDetails) {
        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ALERT_LAT_KEY, details.lat)
            putExtra(ALERT_LONG_KEY, details.lon)
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(
            context,
            Constants.ALERTS_NOTIFICATION_CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.splash_ic)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(
                context.getString(
                    R.string.the_weather_in_will_be,
                    cityName,
                    details.current.weather.first().description
                )
            )
            setContentIntent(notifyPendingIntent)
        }.build()

        NotificationManagerCompat.from(context).notify(121, notification)
    }

    @SuppressLint("InflateParams")
    private suspend fun showSlideDownPopup(cityName: String, details: WeatherDetails) {

        withContext(Dispatchers.Main) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.weather_alert_layout, null)

            popupView.findViewById<TextView>(R.id.tvWeatherDetails).text = context.getString(
                R.string.weather_in_will_be,
                cityName,
                details.current.weather.first().description
            )

            popupView.findViewById<Button>(R.id.btnDismiss).setOnClickListener {
                popupView.animate().apply {
                    translationY(-500f)
                    setDuration(500)
                    withEndAction {
                        windowManager.removeView(popupView)
                    }
                }.start()
            }

            popupView.findViewById<Button>(R.id.btnSnooze).setOnClickListener {
                snoozeAlert(details)
                popupView.animate().apply {
                    translationY(-500f)
                    setDuration(500)
                    withEndAction {
                        windowManager.removeView(popupView)
                    }
                }.start()
            }

            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )

            layoutParams.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL

            playAlertSound()

            windowManager.addView(popupView, layoutParams)

            popupView.translationY = -500f
            popupView.animate()
                .translationY(0f)
                .setDuration(500)
                .start()
        }
    }

    private fun snoozeAlert(details: WeatherDetails) {
        val snoozeRequest = OneTimeWorkRequestBuilder<AlertWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    ALERT_LAT_KEY to details.lat,
                    ALERT_LONG_KEY to details.lon,
                    ALERT_TYPE_KEY to AlertType.POPUP.name
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(snoozeRequest)
    }

    private fun playAlertSound() {
        try {
            val mediaPlayer = MediaPlayer.create(context, R.raw.notification_sound)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLanguage(): String {
        return when (repo.getAppSettings().language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC -> "ar"
            AppLanguage.SAME_AS_DEVICE -> context.resources.configuration.locales[0].language
        }
    }

    companion object {
        const val ALERT_LAT_KEY = "ALERT_LAT_KEY"
        const val ALERT_LONG_KEY = "ALERT_LONG_KEY"
        const val ALERT_TYPE_KEY = "ALERT_TYPE_KEY"
    }

}