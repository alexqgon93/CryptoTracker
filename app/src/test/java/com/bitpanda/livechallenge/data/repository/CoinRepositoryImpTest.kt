package com.bitpanda.livechallenge.data.repository

import arrow.core.Either
import com.bitpanda.livechallenge.data.mappers.AssetsMapper
import com.bitpanda.livechallenge.data.mappers.RatesMapper
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.network.responses.NetworkAsset
import com.bitpanda.livechallenge.network.responses.NetworkAssetResponse
import com.bitpanda.livechallenge.network.responses.NetworkRate
import com.bitpanda.livechallenge.network.responses.NetworkRateResponse
import com.bitpanda.livechallenge.network.source.CoinDataSource
import com.bitpanda.livechallenge.network.source.WebSocketDataSource
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoinRepositoryImpTest {
    private lateinit var coinRepositoryImp: CoinRepositoryImp

    private val assetsMapper: AssetsMapper = mockk()
    private val ratesMapper: RatesMapper = mockk()
    private val dataSource: WebSocketDataSource = mockk()
    private val assetsDataSource: CoinDataSource = mockk()
    private val asset =
        Asset(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            price = 50000.0,
            changePercent24Hr = 2.5,
            rank = 1
        )

    private val rate =
        Rate(
            id = "1",
            symbol = "USD",
            currencySymbol = "$",
            type = "fiat",
            rateUsd = "1.0"
        )

    @BeforeEach
    fun setUp() {
        coinRepositoryImp =
            CoinRepositoryImp(
                assetsMapper = assetsMapper,
                ratesMapper = ratesMapper,
                datasource = dataSource,
                assetsDataSource = assetsDataSource
            )
    }

    @Test
    fun `GIVEN getAssets WHEN correct data THEN mapped correctly`() = runTest {
        val mockResponse =
            NetworkAssetResponse(
                data =
                listOf(
                    NetworkAsset(
                        id = "1",
                        name = "Bitcoin",
                        symbol = "BTC",
                        priceUsd = 50000.0,
                        changePercent24Hr = 2.5,
                        rank = "1"
                    )
                )
            )
        coEvery { assetsDataSource.getAssets() } returns Either.Right(mockResponse)
        coEvery { assetsMapper.map(input = any()) } returns listOf(asset)
        val result = coinRepositoryImp.getAssets()
        result shouldBe Either.Right(value = listOf(asset))
    }

    @Test
    fun `GIVEN getRates WHEN correct data THEN mapped correctly`() = runTest {
        val mockResponse =
            NetworkRateResponse(
                data =
                listOf(
                    NetworkRate(
                        id = "1",
                        symbol = "USD",
                        currencySymbol = "$",
                        type = "fiat",
                        rateUsd = "1.0"
                    )
                )
            )
        coEvery { assetsDataSource.getRates() } returns Either.Right(mockResponse)
        coEvery { ratesMapper.map(input = any()) } returns listOf(rate)
        val result = coinRepositoryImp.getRates()
        result shouldBe Either.Right(value = listOf(rate))
    }

    /*@Test
    fun `GIVEN error response WHEN getAssets is called THEN returns Left AppError`() = runTest {
        val failure = Failure.Unknown(message = "Network Error")
        coEvery { assetsDataSource.getAssets() } returns
            Either.Left(Failure.Unknown(message = "Network Error"))
        val result = coinRepositoryImp.getAssets()
        result shouldBe Either.Left(failure)
    }

    @Test
    fun `GIVEN error response WHEN getRates is called THEN returns Left AppError`() = runTest {
        val failure = Failure.Unknown(message = "Network Error")
        coEvery { assetsDataSource.getRates() } returns Either.Left(failure)
        val result = coinRepositoryImp.getRates()
        result shouldBe Either.Left(failure)
    }*/
}
