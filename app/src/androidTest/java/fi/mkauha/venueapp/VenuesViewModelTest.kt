package fi.mkauha.venueapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import fi.mkauha.venueapp.data.fakes.FakeLocationRepository
import fi.mkauha.venueapp.data.fakes.FakeVenuesRepository
import fi.mkauha.venueapp.ui.VenuesState
import fi.mkauha.venueapp.ui.VenuesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VenuesViewModelTest {
    private lateinit var viewModel: VenuesViewModel
    private val venuesRepository = FakeVenuesRepository()
    private val locationRepository = FakeLocationRepository()



    @Before
    fun setup() {
        viewModel = VenuesViewModel(
            venuesRepository = venuesRepository,
            locationRepository = locationRepository
        )
        locationRepository.startLocationUpdates()
    }

    @Test
    fun stateIsInitial() = runTest {
        assertEquals(VenuesState.Initial, viewModel.state.value)
    }

    @Test
    fun emptyQueryIsReturned() = runTest {
        val query = ""
        viewModel.setQuery(query)

        val result = viewModel.query.value
        assertEquals("" , result)
    }

    @Test
    fun resultsIsReturned() = runTest {
        val query = "test"
        viewModel.setQuery(query)

        assertTrue(viewModel.state.first { it is VenuesState.Loading } is VenuesState.Loading)
        val result = viewModel.state.first { it is VenuesState.Success }
        assertTrue(result is VenuesState.Success && result.venues.isNotEmpty())
    }

}