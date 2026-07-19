package com.cryptotracker.ui.serverError

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cryptotracker.feature.coins.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ServerErrorScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun titleText() = composeRule.activity.getString(R.string.error_message_title)

    private fun retryText() = composeRule.activity.getString(R.string.retry_button_title)

    private fun retryContentDescription() = composeRule.activity.getString(R.string.retry_button_content_description)

    private fun errorIconContentDescription() = composeRule.activity.getString(R.string.error_icon_content_description)

    @Test
    fun serverErrorScreen_displaysTitleAndRetryButton() {
        composeRule.setContent {
            ServerErrorScreen(onRetry = {})
        }
        composeRule.onNodeWithText(titleText()).assertIsDisplayed()
        composeRule.onNodeWithText(retryText()).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(errorIconContentDescription()).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(retryContentDescription()).assertIsDisplayed()
        composeRule.onNodeWithText("Not Found").assertDoesNotExist()
    }

    @Test
    fun serverErrorScreen_retryClick_invokesCallback() {
        var clicks = 0
        composeRule.setContent {
            ServerErrorScreen(onRetry = { clicks++ })
        }
        composeRule.onNodeWithText(retryText()).performClick()
        assertEquals(1, clicks)
    }
}
