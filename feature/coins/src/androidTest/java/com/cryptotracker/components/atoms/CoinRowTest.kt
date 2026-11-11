package com.cryptotracker.components.atoms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cryptotracker.ui.coins.CoinUIModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinRowTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun coinRow_displaysPositiveData() {
        val model =
            CoinUIModel(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                price = "30000",
                changePercent24Hr = 2.5,
                rank = 1,
                priceDifference24Hr = "25.0 €",
            )
        composeTestRule.setContent { CoinRow(uiModel = model) }

        composeTestRule.onNodeWithText(text = "#1").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "BTC").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "30000").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "2,50 %").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "25.0 €").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertDoesNotExist()
    }

    @Test
    fun coinRow_displaysNegativeChange() {
        val model =
            CoinUIModel(
                id = "eth",
                name = "Ethereum",
                symbol = "ETH",
                price = "1800",
                changePercent24Hr = -1.2,
                rank = 2,
                priceDifference24Hr = "-15.4 €",
            )

        composeTestRule.setContent {
            CoinRow(uiModel = model)
        }

        composeTestRule.onNodeWithText(text = "#2").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "ETH").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Ethereum").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "1800").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "-1,20 %").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "-15.4 €").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertDoesNotExist()
    }
}
