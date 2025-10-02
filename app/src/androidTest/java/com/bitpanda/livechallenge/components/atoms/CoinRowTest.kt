package com.bitpanda.livechallenge.components.atoms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bitpanda.livechallenge.ui.coins.CoinUIModel
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
                priceDifference24Hr = 25.0
            )
        composeTestRule.setContent { CoinRow(uiModel = model) }

        composeTestRule.onNodeWithText("#1").assertIsDisplayed()
        composeTestRule.onNodeWithText("BTC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("30000").assertIsDisplayed()
        composeTestRule.onNodeWithText("2.5 %").assertIsDisplayed()
        composeTestRule.onNodeWithText("25.0 €").assertIsDisplayed()
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
                priceDifference24Hr = -15.4
            )

        composeTestRule.setContent {
            CoinRow(uiModel = model)
        }

        composeTestRule.onNodeWithText("#2").assertIsDisplayed()
        composeTestRule.onNodeWithText("ETH").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
        composeTestRule.onNodeWithText("1800").assertIsDisplayed()
        composeTestRule.onNodeWithText("-1.2 %").assertIsDisplayed()
        composeTestRule.onNodeWithText("-15.4 €").assertIsDisplayed()
    }
}
