package com.cryptotracker.data

import com.cryptotracker.data.mappers.RatesMapper
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.network.responses.NetworkRate
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RatesMapperTest {
    private lateinit var mapper: RatesMapper

    @BeforeEach
    fun setUp() {
        mapper = RatesMapper()
    }

    @ParameterizedTest
    @MethodSource("provideDataToTest")
    fun `GIVEN NetworkRates WHEN map THEN return Rate`(
        input: List<NetworkRate>,
        output: List<Rate>,
    ) {
        mapper.map(input) shouldBe output
    }

    private fun provideDataToTest(): Stream<Arguments> = Stream.of(testDataToCheckFileInfoValue())

    private fun testDataToCheckFileInfoValue() =
        Arguments.of(
            listOf(
                NetworkRate(
                    id = "united-states-dollar",
                    symbol = "USD",
                    currencySymbol = "$",
                    rateUsd = "1.0000000000000000",
                    type = "fiat",
                ),
            ),
            listOf(
                Rate(
                    id = "united-states-dollar",
                    symbol = "USD",
                    currencySymbol = "$",
                    rateUsd = "1.0000000000000000",
                    type = "fiat",
                ),
            ),
        )
}
