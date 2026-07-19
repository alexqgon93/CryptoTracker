package com.cryptotracker.data

import com.cryptotracker.data.mappers.AssetsMapper
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.network.responses.NetworkAsset
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AssetsMapperTest {
    private val mapper = AssetsMapper()

    @Test
    fun `Given data WHEN map THEN return domain model`() {
        val networkAssets =
            listOf(
                NetworkAsset(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    priceUsd = 50000.0,
                    changePercent24Hr = 5.0,
                    rank = "1",
                ),
                NetworkAsset(
                    id = "2",
                    name = "Ethereum",
                    symbol = "ETH",
                    priceUsd = 4000.0,
                    changePercent24Hr = 3.0,
                    rank = "2",
                ),
            )
        val result = mapper.map(networkAssets)
        val expected =
            listOf(
                Asset(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    price = 50000.0,
                    changePercent24Hr = 5.0,
                    rank = 1,
                ),
                Asset(
                    id = "2",
                    name = "Ethereum",
                    symbol = "ETH",
                    price = 4000.0,
                    changePercent24Hr = 3.0,
                    rank = 2,
                ),
            )
        assert(expected == result)
    }

    @Test
    fun `Given malformed rank WHEN map THEN throws number format exception`() {
        val malformedAsset =
            listOf(
                NetworkAsset(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    priceUsd = 50000.0,
                    changePercent24Hr = 5.0,
                    rank = "not-a-number",
                ),
            )

        assertThrows(NumberFormatException::class.java) {
            mapper.map(malformedAsset)
        }
    }
}
