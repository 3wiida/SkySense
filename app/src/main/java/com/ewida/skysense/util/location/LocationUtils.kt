package com.ewida.skysense.util.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import com.ewida.skysense.util.roundTo
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
                Log.d("```TAG```", "lat: ${location.latitude.roundTo(2)}")
                Log.d("```TAG```", "lon: ${location.longitude.roundTo(2)}")
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
        location: Location,
        onAddressAvailable: (String) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(context).getFromLocation(
                location.latitude,
                location.longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address?>) {
                        addresses.firstOrNull()?.let { address ->
                            val addressLine = "${address.countryName}, ${address.adminArea}"
                            onAddressAvailable(addressLine)
                        }
                    }
                }
            )
        } else {
            val addresses = Geocoder(context).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            addresses?.firstOrNull()?.let { address ->
                val addressLine = "${address.countryName}, ${address.adminArea}"
                onAddressAvailable(addressLine)
            }
        }
    }

    fun getLocationAddressLine(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): String {
        val addresses = Geocoder(context).getFromLocation(
            latitude,
            longitude,
            1
        )
        return if (addresses != null && addresses.isNotEmpty()) {
            "${addresses.first().adminArea}, ${addresses.first().subAdminArea}"
        } else {
            "Unknown Location"
        }
    }

}