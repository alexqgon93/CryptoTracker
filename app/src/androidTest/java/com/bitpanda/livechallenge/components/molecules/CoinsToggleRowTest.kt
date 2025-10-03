package com.bitpanda.livechallenge.components.molecules

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bitpanda.livechallenge.R
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CoinsToggleRowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun topText() = composeRule.activity.getString(R.string.trending_top_title)
    private fun bottomText() = composeRule.activity.getString(R.string.trending_lowest_title)

    @Test
    fun initialState_isTopTrue_selectsTopOnly() {
        composeRule.setContent { CoinsToggleRow(isTop = true, onToggle = {}) }
        composeRule.onNodeWithText(topText()).assertIsSelected()
        composeRule.onNodeWithText(bottomText()).assertIsNotSelected()
    }

    @Test
    fun initialState_isTopFalse_selectsBottomOnly() {
        composeRule.setContent { CoinsToggleRow(isTop = false, onToggle = {}) }
        composeRule.onNodeWithText(topText()).assertIsNotSelected()
        composeRule.onNodeWithText(bottomText()).assertIsSelected()
    }

    @Test
    fun clickingBottom_emitsEventAndTogglesSelection() {
        var receivedEvent: UiEvent? = null
        composeRule.setContent {
            var isTop by remember { mutableStateOf(true) }
            CoinsToggleRow(
                isTop = isTop,
                onToggle = { event ->
                    receivedEvent = event
                    when (event) {
                        UiEvent.OnTopCoinsClick -> isTop = true
                        UiEvent.OnBottomCoinsClick -> isTop = false
                        else -> {}
                    }
                }
            )
        }
        composeRule.onNodeWithText(topText()).assertIsSelected()
        composeRule.onNodeWithText(bottomText()).assertIsNotSelected()
        composeRule.onNodeWithText(bottomText()).performClick()
        composeRule.onNodeWithText(topText()).assertIsNotSelected()
        composeRule.onNodeWithText(bottomText()).assertIsSelected()
        assertEquals(UiEvent.OnBottomCoinsClick, receivedEvent)
    }

    @Test
    fun clickingTopAfterBottom_reselectsTop() {
        composeRule.setContent {
            var isTop by remember { mutableStateOf(false) }
            CoinsToggleRow(
                isTop = isTop,
                onToggle = { event ->
                    when (event) {
                        UiEvent.OnTopCoinsClick -> isTop = true
                        UiEvent.OnBottomCoinsClick -> isTop = false
                        else -> {}
                    }
                }
            )
        }
        composeRule.onNodeWithText(topText()).assertIsNotSelected()
        composeRule.onNodeWithText(bottomText()).assertIsSelected()
        composeRule.onNodeWithText(topText()).performClick()
        composeRule.onNodeWithText(topText()).assertIsSelected()
        composeRule.onNodeWithText(bottomText()).assertIsNotSelected()
    }

    @Test
    fun reClickingSelectedButton_keepsSelection() {
        composeRule.setContent {
            var isTop by remember { mutableStateOf(true) }
            CoinsToggleRow(
                isTop = isTop,
                onToggle = { event ->
                    if (event == UiEvent.OnTopCoinsClick) isTop = true
                }
            )
        }
        composeRule.onNodeWithText(topText()).performClick()
        composeRule.onNodeWithText(topText()).assertIsSelected()
        composeRule.onNodeWithText(bottomText()).assertIsNotSelected()
    }
}
