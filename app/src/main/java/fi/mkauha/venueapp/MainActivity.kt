package fi.mkauha.venueapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import fi.mkauha.venueapp.ui.VenuesApp
import fi.mkauha.venueapp.ui.theme.VenueAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VenueAppTheme {
                VenuesApp()
            }
        }
    }
}

