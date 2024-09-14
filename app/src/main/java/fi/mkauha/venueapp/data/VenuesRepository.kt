package fi.mkauha.venueapp.data

import fi.mkauha.venueapp.data.interfaces.IVenuesRepository
import fi.mkauha.venueapp.model.Venue
import fi.mkauha.venueapp.network.VenuesApi
import fi.mkauha.venueapp.network.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    private val api: VenuesApi
): IVenuesRepository {
    override suspend fun getVenuesByQuery(query: String, location: String?): Flow<List<Venue>> {
        val latLng = location ?: "60.192059,24.945831"
        return flow {
            val places = api.searchVenues(query, latLng, "RELEVANCE")
                .searchResponse.venues.map { it.toDomain() }
                .sortedBy { it.location.distance }
            emit(places)

        }.flowOn(Dispatchers.IO)
    }

}


