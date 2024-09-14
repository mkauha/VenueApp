package fi.mkauha.venueapp.data.interfaces

import kotlinx.coroutines.flow.StateFlow

interface ILocationRepository {
    val locationFlow: StateFlow<String?>
    fun startLocationUpdates()
    fun stopLocationUpdates()
}