package com.ewida.skysense.data.sources.remote.api

import android.util.Log
import com.ewida.skysense.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/3.0/"
    private const val NETWORK_LOG_TAG = "NetworkResponse"
    private const val API_KEY_QUERY_KEY = "appid"
    private const val API_KEY_QUERY_VALUE = BuildConfig.OPEN_WEATHER_MAP_API_KEY

    fun getApiServices(): ApiServices = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(getHttpClient())
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(ApiServices::class.java)

    private fun getHttpClient() = OkHttpClient.Builder().apply {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.i(NETWORK_LOG_TAG, message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)


        addInterceptor { chain ->
            val request = chain.request()
            val originalUrl = request.url
            val newUrl = originalUrl.newBuilder().addQueryParameter(
                API_KEY_QUERY_KEY,
                API_KEY_QUERY_VALUE
            ).build()

            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }
    }.build()
}