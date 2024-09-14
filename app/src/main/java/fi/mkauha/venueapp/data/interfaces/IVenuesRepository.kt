package fi.mkauha.venueapp.data.interfaces

import fi.mkauha.venueapp.model.Venue
import kotlinx.coroutines.flow.Flow

interface IVenuesRepository {
    suspend fun getVenuesByQuery(query: String, location: String?): Flow<List<Venue>>
}