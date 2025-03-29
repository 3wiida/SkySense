package com.ewida.skysense.workers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ewida.skysense.MainActivity
import com.ewida.skysense.R
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.remote.api.ApiClient
import com.ewida.skysense.util.Constants
import com.ewida.skysense.util.enums.AlertType
import com.ewida.skysense.util.location.LocationUtils
import com.ewida.skysense.data.repository.WeatherRepositoryImpl
import com.ewida.skysense.data.sources.local.LocalDataSourceImpl
import com.ewida.skysense.data.sources.local.db.WeatherDatabase
import com.ewida.skysense.data.sources.remote.RemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AlertWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repo = WeatherRepositoryImpl.getInstance(
        localDataSource = LocalDataSourceImpl(dao = WeatherDatabase.getInstance(context).getDao()),
        remoteDataSource = RemoteDataSourceImpl(apiServices = ApiClient.getApiServices())
    )

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val lat = inputData.getDouble(ALERT_LAT_KEY, 0.0)
        val long = inputData.getDouble(ALERT_LONG_KEY, 0.0)
        //val date = inputData.getString(ALERT_DATE_KEY)
        //val time = inputData.getString(ALERT_TIME_KEY)
        val type = inputData.getString(ALERT_TYPE_KEY)

        val cityName = LocationUtils.getLocationAddressLine(context, lat, long)?.subAdminArea
            ?: "Unknown Location"
        val weatherDetails = repo.getRemoteWeatherDetails(lat, long)

        when (type) {
            AlertType.NOTIFICATION.name -> {
                notify(cityName, weatherDetails)
            }

            AlertType.POPUP.name -> {
                showSlideDownPopup(cityName, weatherDetails)
            }
        }

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
            setContentText("The weather in $cityName will be ${details.current.weather.first().description}")
            setContentIntent(notifyPendingIntent)
        }.build()

        NotificationManagerCompat.from(context).notify(121, notification)
    }

    @SuppressLint("InflateParams")
    private suspend fun showSlideDownPopup(cityName: String, details: WeatherDetails) {

        withContext(Dispatchers.Main) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            // Inflate XML layout
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.weather_alert_layout, null)


            popupView.findViewById<TextView>(R.id.tvWeatherDetails).text = "Weather in $cityName will be ${details.current.weather.first().description}"
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

            }

            // WindowManager parameters
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
            layoutParams.gravity = Gravity.TOP

            playAlertSound()

            windowManager.addView(popupView, layoutParams)

            popupView.translationY = -500f
            popupView.animate()
                .translationY(0f)
                .setDuration(500)
                .start()
        }
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

    companion object {
        const val ALERT_LAT_KEY = "ALERT_LAT_KEY"
        const val ALERT_LONG_KEY = "ALERT_LONG_KEY"
        const val ALERT_DATE_KEY = "ALERT_DATE_KEY"
        const val ALERT_TIME_KEY = "ALERT_TIME_KEY"
        const val ALERT_TYPE_KEY = "ALERT_TYPE_KEY"
    }

}