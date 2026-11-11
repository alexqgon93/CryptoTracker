package com.cryptotracker.domain.models

import org.junit.jupiter.api.Test

class AssetTest {
    val asset =
        Asset(
            id = "1",
            name = "Bitcoin",
            rank = 1,
            symbol = "BTC",
            price = 100.0,
            changePercent24Hr = 10.0,
        )

    @Test
    fun `GIVEN zero percent WHEN priceDifference24Hr is called THEN returns zero`() =
        assert(0.0 == asset.priceDifference24Hr(changePercent = 0.0))

    @Test
    fun `GIVEN positive percent WHEN priceDifference24Hr is called THEN returns correct value`() =
        assert(10.0 == asset.priceDifference24Hr(changePercent = 10.0))

    @Test
    fun `GIVEN negative percent WHEN priceDifference24Hr THEN returns correct negative value`() =
        assert(-10.0 == asset.priceDifference24Hr(changePercent = -10.0))

    @Test
    fun `GIVEN large percent WHEN priceDifference24Hr is called THEN returns correct value`() =
        assert(200.0 == asset.priceDifference24Hr(changePercent = 200.0))

    @Test
    fun `GIVEN decimal percent WHEN priceDifference24Hr THEN returns correct decimal value`() =
        assert(2.5 == asset.priceDifference24Hr(changePercent = 2.5))

    @Test
    fun `GIVEN price zero WHEN priceDifference24Hr is called THEN returns zero`() =
        assert(0.0 == asset.copy(price = 0.0).priceDifference24Hr(changePercent = 10.0))

    @Test
    fun `GIVEN price and percent zero WHEN priceDifference24Hr is called THEN returns zero`() =
        assert(0.0 == asset.copy(price = 0.0).priceDifference24Hr(changePercent = 0.0))
}
