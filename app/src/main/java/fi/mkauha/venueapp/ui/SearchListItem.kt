package fi.mkauha.venueapp.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import fi.mkauha.venueapp.model.Venue

@Composable
fun SearchListItem(venue: Venue) {
    ListItem(
        modifier = Modifier.testTag("searchListItem"),
        headlineContent = { Text(venue.name) },
        supportingContent = { Text(venue.location.formattedAddress()) },
        trailingContent = { Text("${venue.location.distance} m") },
    )
    HorizontalDivider()
}