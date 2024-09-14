package fi.mkauha.venueapp.data.fakes

import fi.mkauha.venueapp.data.interfaces.IVenuesRepository
import fi.mkauha.venueapp.model.Location
import fi.mkauha.venueapp.model.Venue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class FakeVenuesRepository : IVenuesRepository{


    private val venues = MutableStateFlow(fakeVenues)

    override suspend fun getVenuesByQuery(query: String, location: String?): Flow<List<Venue>> {
        return flow {
            delay(500)
            emitAll(venues)
        }
    }
}