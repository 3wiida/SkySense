package com.ewida.skysense.util.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onLocationAvailable: (location: Location) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val callbacks = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                onLocationAvailable(location)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 0).build(),
            callbacks,
            Looper.getMainLooper()
        )
    }

    fun getLocationAddressLine(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): Address? {
        return try {
            val addresses = Geocoder(context).getFromLocation(
                latitude,
                longitude,
                1
            )
            addresses?.firstOrNull()
        }catch (_: Exception){
            null
        }
    }

}