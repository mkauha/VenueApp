package fi.mkauha.venueapp.data

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import fi.mkauha.venueapp.data.interfaces.ILocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationManager: LocationManager,
):ILocationRepository {
    private val tag = "LocationRepository"

    private val _locationFlow = MutableStateFlow<String?>(null)
    override val locationFlow: StateFlow<String?> get() = _locationFlow

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latLng = "${location.latitude},${location.longitude}"
            Log.d(tag, latLng)
            _locationFlow.value = latLng
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                5f,
                locationListener
            )
        }
    }

    override fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }
}