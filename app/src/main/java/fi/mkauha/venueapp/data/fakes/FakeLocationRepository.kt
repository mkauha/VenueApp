package fi.mkauha.venueapp.data.fakes

import fi.mkauha.venueapp.data.interfaces.ILocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeLocationRepository : ILocationRepository {
    private val _locationFlow = MutableStateFlow<String?>(null)
    override val locationFlow: StateFlow<String?> get() = _locationFlow

    override fun startLocationUpdates() {
        _locationFlow.value = "60.192059,24.945831"
    }

    override fun stopLocationUpdates() {}

}