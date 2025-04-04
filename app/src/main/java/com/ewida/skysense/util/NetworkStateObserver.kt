package com.ewida.skysense.util


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

class NetworkStateObserver(
    private val context: Context,
    private val networkCallbacksListener: NetworkCallbacksListener
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkCallbacksListener.onConnectionAvailable()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkCallbacksListener.onConnectionUnAvailable()
    }



    fun register() {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        cm?.registerNetworkCallback(networkRequest, this)
    }

    fun unregister() {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        cm?.unregisterNetworkCallback(this)
    }

    interface NetworkCallbacksListener {
        fun onConnectionAvailable()
        fun onConnectionUnAvailable()
    }
}

