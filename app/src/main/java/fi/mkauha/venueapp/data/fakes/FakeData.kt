package fi.mkauha.venueapp.data.fakes

import fi.mkauha.venueapp.model.Location
import fi.mkauha.venueapp.model.Venue

val fakeVenues = listOf(
    Venue(
        id = "1",
        name = "Paikka A",
        location = Location(
            address = "Osoite 1",
            city = "Tampere",
            postalCode = "33100",
            distance = 1000
        ),

        ),
    Venue(
        id = "2",
        name = "Paikka B",
        location = Location(address = "Osoite 2",
            city = "Tampere",
            postalCode = "33100",
            distance = 1000
        ),
    )
)