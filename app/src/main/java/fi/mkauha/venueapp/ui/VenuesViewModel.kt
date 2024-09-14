package fi.mkauha.venueapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fi.mkauha.venueapp.data.interfaces.ILocationRepository
import fi.mkauha.venueapp.data.interfaces.IVenuesRepository
import fi.mkauha.venueapp.model.Venue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class VenuesViewModel @Inject constructor(
    private val venuesRepository: IVenuesRepository,
    private val locationRepository: ILocationRepository,
) : ViewModel() {
    private val tag = "VenuesViewModel"

    private val _state = MutableStateFlow<VenuesState>(VenuesState.Initial)
    val state: StateFlow<VenuesState> get() = _state

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> get() = _locationPermissionGranted

    val query = MutableStateFlow("")

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun resetQuery() {
        setQuery("")
        _state.value = VenuesState.Success(listOf())
    }

    fun setLocationPermissions(isGranted: Boolean) {
        _locationPermissionGranted.value = isGranted
        if (isGranted) locationRepository.startLocationUpdates()
    }

    init {
        viewModelScope.launch {
            query
                .debounce(300)
                .collect {
                    if(it.trim().isNotEmpty()) {
                        try {
                            _state.value = VenuesState.Loading

                            var location = locationRepository.locationFlow.value
                            repeat(3) {
                                if (location != null) return@repeat
                                delay(500)
                                location = locationRepository.locationFlow.value
                            }

                            venuesRepository.getVenuesByQuery(it, location).collect { venues ->
                                _state.value = VenuesState.Success(venues)
                            }
                        } catch (e: Throwable) {
                            _state.value = VenuesState.Error(e)
                            print(e.message)
                        }
                    } else {
                        _state.value = VenuesState.Initial
                    }
                }
        }
    }
}

sealed class VenuesState {
    data object Initial: VenuesState()
    data class Success(val venues: List<Venue>): VenuesState()
    data class Error(val exception: Throwable): VenuesState()
    data object Loading: VenuesState()
}