package fi.mkauha.venueapp

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import fi.mkauha.venueapp.data.fakes.FakeLocationRepository
import fi.mkauha.venueapp.data.fakes.FakeVenuesRepository
import fi.mkauha.venueapp.ui.VenuesApp
import fi.mkauha.venueapp.ui.VenuesViewModel
import fi.mkauha.venueapp.ui.theme.VenueAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VenueAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var viewModel: VenuesViewModel
    private val venuesRepository = FakeVenuesRepository()
    private val locationRepository = FakeLocationRepository()

    @Before
    fun setup() {
        viewModel = VenuesViewModel(
            venuesRepository = venuesRepository,
            locationRepository = locationRepository
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchTest() {

        composeTestRule.activity.setContent {
            VenueAppTheme {
                VenuesApp(viewModel)
            }
        }

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        val allowButton = device.findObject(UiSelector().text("While using the app"))
        if (allowButton.exists()) {
            allowButton.click()
        }

        composeTestRule.onNodeWithText("Search for venues").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").performTextInput("R")
        composeTestRule.onNode(hasTestTag("loadingIndicator")).assertIsDisplayed()

        composeTestRule.waitUntilDoesNotExist(
            hasTestTag("loadingIndicator"),
            timeoutMillis = 1000
        )
        composeTestRule.onAllNodesWithTag("searchListItem").assertCountEquals(2)
    }
}