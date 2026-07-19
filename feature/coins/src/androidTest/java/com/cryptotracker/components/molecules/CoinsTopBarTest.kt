package com.cryptotracker.components.molecules

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class CoinsTopBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        const val TITLE = "Cryptocurrency Prices"
    }

    @Test
    fun coinsTopBar_displaysTitle() {
        composeTestRule.setContent {
            CoinsTopBar(title = TITLE)
        }
        composeTestRule.onNodeWithText(text = TITLE).assertIsDisplayed()
    }

    @Test
    fun coinsTopBar_displaysOnlyCorrectTitle() {
        val wrongTitle = "Market Overview"
        composeTestRule.setContent {
            CoinsTopBar(title = TITLE)
        }
        composeTestRule.onNodeWithText(text = TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithText(text = wrongTitle).assertDoesNotExist()
    }
}
