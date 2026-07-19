package com.cryptotracker.components.atoms

import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OutlinedToggleButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun outlinedToggleButton_displaysCorrectly() {
        composeTestRule.setContent {
            OutlinedToggleButton(
                text = "Test Button",
                selected = true,
                icon = Filled.TrendingUp,
                onClick = {},
                contentDescription = "Test Button",
            )
        }
        composeTestRule.onNodeWithText("Test Button").assertExists()
    }

    @Test
    fun outlinedToggleButton_invokesOnClick() {
        var clicked = false
        composeTestRule.setContent {
            OutlinedToggleButton(
                selected = false,
                onClick = { clicked = true },
                icon = Filled.TrendingUp,
                text = "Tap",
                contentDescription = "Tap Icon",
            )
        }
        composeTestRule.onNodeWithText("Tap").performClick()
        assert(clicked)
    }
}
