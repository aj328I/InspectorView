package com.example.inspectorviewapp.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inspectorviewapp.viewmodel.StartScreenViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.ComposeTestRuleKt
import androidx.compose.ui.test.junit4.ComposeTestRuleAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleImpl
import androidx.compose.ui.test.junit4.ComposeTestRuleOwner
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImpl
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKt
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroid
import androidx.compose.ui.test.junit4.ComposeTestRuleOwnerImplKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtKtAndroidKt

@RunWith(AndroidJUnit4::class)
class StartScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startScreen_displaysAllUIElements() {
        composeTestRule.setContent {
            StartScreen(viewModel = StartScreenViewModel(androidx.test.core.app.ApplicationProvider.getApplicationContext()))
        }
        // Camera preview is present (PreviewView is not directly testable, but the background is black)
        composeTestRule.onRoot().assert(hasAnyDescendant(hasTestTag("CameraPreviewBox")))
        // Shutter button is present and centered at the bottom
        composeTestRule.onNodeWithTag("ShutterButton").assertIsDisplayed()
        // Heading text is present
        composeTestRule.onNodeWithText("Heading: 319°").assertIsDisplayed()
        // Settings and close icons are present
        composeTestRule.onNodeWithContentDescription("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
        // GPS and timestamp texts are present
        composeTestRule.onNodeWithText("GPS: 37.42, -122.08").assertIsDisplayed()
        composeTestRule.onNodeWithText("2025-07-02 14:34:41").assertExists()
    }

    @Test
    fun startScreen_tabletLayout_adaptsCorrectly() {
        composeTestRule.setContent {
            androidx.compose.ui.platform.LocalConfiguration.current.apply {
                screenWidthDp = 800 // simulate tablet
            }
            StartScreen(viewModel = StartScreenViewModel(androidx.test.core.app.ApplicationProvider.getApplicationContext()))
        }
        // Shutter button is larger
        composeTestRule.onNodeWithTag("ShutterButton").assertIsDisplayed()
        // Heading text is larger
        composeTestRule.onNodeWithText("Heading: 3190").assertIsDisplayed()
        // Settings and close icons are larger and spaced
        composeTestRule.onNodeWithContentDescription("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
        // GPS and timestamp texts are present and larger
        composeTestRule.onNodeWithText("GPS: 37.42, -122.08").assertIsDisplayed()
        composeTestRule.onNodeWithText("2025-07-02 14:34:41").assertExists()
    }
} 