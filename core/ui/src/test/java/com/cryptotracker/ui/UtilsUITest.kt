package com.cryptotracker.ui

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UtilsUITest {
    @Test
    fun `GIVEN a double value WHEN formatToTwoDigits is called THEN it rounds to two digits`() =
        Assertions.assertEquals(3.14, 3.14159.formatToTwoDigits())

    @Test
    fun `GIVEN double value WHEN toCorrectCurrencyForm THEN it returns euro string format`() =
        Assertions.assertEquals(
            "1.234,57 €",
            1234.567.toCorrectCurrencyForm().replace('\u00A0', ' '),
        )

    @Test
    fun `GIVEN double value WHEN formatToTwoDigits THEN it rounds to two digits correctly`() =
        Assertions.assertEquals(2.72, 2.71828.formatToTwoDigits())
}
