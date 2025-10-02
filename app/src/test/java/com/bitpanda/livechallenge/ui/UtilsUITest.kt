package com.bitpanda.livechallenge.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsUITest {
    @Test
    fun `format should round double to given digits`() {
        val value = 3.14159
        val result = value.format(2)
        assertEquals(3.14, result, 0.0)
    }

    @Test
    fun `toEuroString should format double as euro string`() {
        val value = 1234.567
        val result = value.toEuroString()
        assertEquals("1,234.57 €", result)
    }

    @Test
    fun `formatToTwoDigits should round double to two digits`() {
        val value = 2.71828
        val result = value.formatToTwoDigits()
        assertEquals(2.72, result, 0.0)
    }
}
