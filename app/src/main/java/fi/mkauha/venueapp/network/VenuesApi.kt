package fi.mkauha.venueapp.network

import com.google.gson.annotations.SerializedName
import fi.mkauha.venueapp.model.Location
import fi.mkauha.venueapp.model.Venue
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesApi {

    companion object {
        const val BASE_URL = "https://api.foursquare.com/" // TODO move to buildconfig
    }

    @GET("/v2/venues/search")
    suspend fun searchVenues(
        @Query("query") query: String,
        @Query("ll") coordinates: String,
        @Query("sort") sort: String,
    ): SearchResponse

}

data class SearchResponse(
    @SerializedName("response") val searchResponse: VenuesListResponse
)

data class VenuesListResponse(
    @SerializedName("venues") val venues: List<VenueResponse> = listOf()
)

data class VenueResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: LocationResponse
)

fun VenueResponse.toDomain(): Venue =
    Venue(
        id = id,
        name = name,
        location = location.toDomain()
    )

data class LocationResponse(
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("postalCode") val postalCode: String?,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("distance") val distance: Int,
)

fun LocationResponse.toDomain(): Location =
    Location(
        address = address,
        city = city,
        postalCode = postalCode,
        distance = distance,
    )
