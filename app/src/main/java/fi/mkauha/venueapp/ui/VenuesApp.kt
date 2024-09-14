package fi.mkauha.venueapp.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import fi.mkauha.venueapp.data.fakes.fakeVenues
import fi.mkauha.venueapp.model.Location
import fi.mkauha.venueapp.model.Venue

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VenuesApp(
    viewModel: VenuesViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val searchQuery = viewModel.query.collectAsState()
    val hasLocation = viewModel.locationPermissionGranted.collectAsState()

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    viewModel.setLocationPermissions(locationPermissionsState.allPermissionsGranted)

    LaunchedEffect(locationPermissionsState) {
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    return VenuesContent(
        state = state,
        searchQuery = searchQuery.value,
        hasLocation = hasLocation.value,
        onQueryUpdated = { value -> viewModel.setQuery(value)},
        onQueryCleared = { viewModel.resetQuery() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuesContent(
    state: VenuesState,
    searchQuery: String,
    onQueryUpdated: (String) -> Unit = {},
    onQueryCleared: () -> Unit = {},
    hasLocation: Boolean,
) {
    return Scaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Venues app")
                }
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(
                    vertical = innerPadding.calculateTopPadding(),
                    horizontal = 16.dp
                )
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { value -> onQueryUpdated(value) },
                label = { Text("Search") },
                supportingText = {
                    if (!hasLocation) Text("Unable to get location")
                },
                trailingIcon = {
                    if(searchQuery.isNotEmpty()) IconButton(
                        onClick = { onQueryCleared() },
                    ) {
                        Icon(Icons.Outlined.Clear, "Clear")
                    }
                }
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                when(state) {
                    VenuesState.Initial -> Text("Search for venues")
                    VenuesState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.testTag("loadingIndicator")
                    )
                    is VenuesState.Success ->
                        if (state.venues.isEmpty() && searchQuery.isNotEmpty()) {
                            Text("No results")
                        }
                        else LazyColumn {
                            items(state.venues) {
                                SearchListItem(it)
                            }
                        }
                    is VenuesState.Error -> Text("Unable to fetch results: ${state.exception}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    val successState = VenuesState.Success(venues = fakeVenues)
    VenuesContent(
        onQueryUpdated = {},
        searchQuery = "",
        hasLocation = false,
        state = successState
    )
}