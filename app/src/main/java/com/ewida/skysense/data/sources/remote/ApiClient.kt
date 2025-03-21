package com.ewida.skysense.data.sources.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    private const val NETWORK_LOG_TAG = "NetworkResponse"
    private const val API_KEY_QUERY_KEY = "appid"
    private const val API_KEY_QUERY_VALUE = "2fec6deee3ff318e2cd9561812106598"

    fun getApiServices() = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(getHttpClient())
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(ApiServices::class.java)

    private fun getHttpClient() = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor { networkLog ->
                Log.i(NETWORK_LOG_TAG, networkLog)
            }
        )

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