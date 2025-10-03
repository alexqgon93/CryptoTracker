package com.bitpanda.livechallenge.ui.coins

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bitpanda.livechallenge.R
import com.bitpanda.livechallenge.ui.coins.CoinsContract.State
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CoinScreenContentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun titleText() = composeRule.activity.getString(R.string.cryptocurrency_prices_title)
    private fun topToggleText() = composeRule.activity.getString(R.string.trending_top_title)
    private fun bottomToggleText() = composeRule.activity.getString(R.string.trending_lowest_title)
    private fun retryText() = composeRule.activity.getString(R.string.retry_button_title)

    private fun coin(name: String, symbol: String) = CoinUIModel(
        id = name.lowercase(),
        name = name,
        symbol = symbol,
        price = "123.45",
        changePercent24Hr = 1.0,
        rank = 1,
        priceDifference24Hr = "2.0"
    )

    @Test
    fun loadingState_showsOnlyProgress() {
        composeRule.setContent {
            CoinScreenContent(
                state = State(
                    screenState = ScreenState.LOADING,
                    coins = null,
                    isTop = true
                ),
                onPushEvent = {}
            )
        }
        composeRule.onNodeWithText(titleText()).assertDoesNotExist()
        composeRule.onNodeWithText(topToggleText()).assertDoesNotExist()
        composeRule.onNodeWithText(bottomToggleText()).assertDoesNotExist()
        composeRule.onNodeWithText(retryText()).assertDoesNotExist()
    }

    @Test
    fun successState_displaysCoinsAndTopBar() {
        val coins = listOf(
            coin("Bitcoin", "BTC"),
            coin("Ethereum", "ETH")
        )

        composeRule.setContent {
            CoinScreenContent(
                state = State(
                    screenState = ScreenState.SUCCESS,
                    coins = coins,
                    isTop = true
                ),
                onPushEvent = {}
            )
        }

        composeRule.onNodeWithText(titleText()).assertIsDisplayed()
        composeRule.onNodeWithText(topToggleText()).assertIsDisplayed()
        composeRule.onNodeWithText(bottomToggleText()).assertIsDisplayed()
        composeRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeRule.onNodeWithText("Ethereum").assertIsDisplayed()
        composeRule.onNodeWithText("Litecoin").assertDoesNotExist()
        composeRule.onNodeWithText(retryText()).assertDoesNotExist()
    }

    @Test
    fun successState_toggleClicksEmitUiEvents() {
        val coins = listOf(coin("Bitcoin", "BTC"))
        val events = mutableListOf<UiEvent>()

        composeRule.setContent {
            CoinScreenContent(
                state = State(
                    screenState = ScreenState.SUCCESS,
                    coins = coins,
                    isTop = true
                ),
                onPushEvent = { events.add(it) }
            )
        }
        composeRule.onNodeWithText(bottomToggleText()).performClick()
        composeRule.onNodeWithText(topToggleText()).performClick()
        assertEquals(listOf(UiEvent.OnBottomCoinsClick, UiEvent.OnTopCoinsClick), events)
    }

    @Test
    fun errorState_showsRetryAndEmitsEvent() {
        val events = mutableListOf<UiEvent>()
        composeRule.setContent {
            CoinScreenContent(
                state = State(
                    screenState = ScreenState.ERROR,
                    coins = null,
                    isTop = true
                ),
                onPushEvent = { events.add(it) }
            )
        }
        composeRule.onNodeWithText(retryText()).assertIsDisplayed()
        composeRule.onNodeWithText(titleText()).assertDoesNotExist()
        composeRule.onNodeWithText(retryText()).performClick()
        assertEquals(listOf(UiEvent.OnRetryButtonClicked), events)
    }
}
