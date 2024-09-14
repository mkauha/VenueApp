package fi.mkauha.venueapp.model

data class Location(
    val address: String?,
    val city: String?,
    val postalCode: String?,
    val distance: Int,
) {
    fun formattedAddress(): String {
        return when {
            address != null && postalCode != null && city != null-> "$address, $postalCode $city"
            postalCode != null && city != null -> "$postalCode $city"
            else -> "-"
        }
    }
}