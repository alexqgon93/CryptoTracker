package com.cryptotracker.data.repository

import arrow.core.Either
import com.cryptotracker.data.mappers.AssetsMapper
import com.cryptotracker.data.mappers.RatesMapper
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.network.responses.NetworkAsset
import com.cryptotracker.network.responses.NetworkAssetResponse
import com.cryptotracker.network.responses.NetworkRate
import com.cryptotracker.network.responses.NetworkRateResponse
import com.cryptotracker.network.source.CoinDataSource
import com.cryptotracker.network.utils.Failure
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoinRepositoryImpTest {
    private lateinit var coinRepositoryImp: CoinRepositoryImp

    private val assetsMapper: AssetsMapper = mockk()
    private val ratesMapper: RatesMapper = mockk()
    private val assetsDataSource: CoinDataSource = mockk()
    private val asset =
        Asset(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            price = 50000.0,
            changePercent24Hr = 2.5,
            rank = 1,
        )

    private val rate =
        Rate(
            id = "1",
            symbol = "USD",
            currencySymbol = "$",
            type = "fiat",
            rateUsd = "1.0",
        )

    @BeforeEach
    fun setUp() {
        coinRepositoryImp =
            CoinRepositoryImp(
                assetsMapper = assetsMapper,
                ratesMapper = ratesMapper,
                assetsDataSource = assetsDataSource,
            )
    }

    @Test
    fun `GIVEN getAssets WHEN correct data THEN mapped correctly`() =
        runTest {
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
                                rank = "1",
                            ),
                        ),
                )
            coEvery { assetsDataSource.getAssets() } returns Either.Right(mockResponse)
            coEvery { assetsMapper.map(input = any()) } returns listOf(asset)
            val result = coinRepositoryImp.getAssets()
            result shouldBe Either.Right(value = listOf(asset))
        }

    @Test
    fun `GIVEN getRates WHEN correct data THEN mapped correctly`() =
        runTest {
            val mockResponse =
                NetworkRateResponse(
                    data =
                        listOf(
                            NetworkRate(
                                id = "1",
                                symbol = "USD",
                                currencySymbol = "$",
                                type = "fiat",
                                rateUsd = "1.0",
                            ),
                        ),
                )
            coEvery { assetsDataSource.getRates() } returns Either.Right(mockResponse)
            coEvery { ratesMapper.map(input = any()) } returns listOf(rate)
            val result = coinRepositoryImp.getRates()
            result shouldBe Either.Right(value = listOf(rate))
        }

    @Test
    fun `GIVEN error response WHEN getAssets is called THEN returns Left AppError`() =
        runTest {
            coEvery { assetsDataSource.getAssets() } returns Either.Left(Failure.Unknown(message = "Network Error"))

            val result = coinRepositoryImp.getAssets()

            result shouldBe Either.Left(AppError.Unknown(message = "Network Error"))
        }

    @Test
    fun `GIVEN error response WHEN getRates is called THEN returns Left AppError`() =
        runTest {
            coEvery { assetsDataSource.getRates() } returns Either.Left(Failure.BadRequest(code = 400, message = "Bad data"))

            val result = coinRepositoryImp.getRates()

            result shouldBe Either.Left(AppError.BadRequest(message = "Bad data"))
        }

    @Test
    fun `GIVEN malformed asset payload WHEN getAssets maps data THEN propagates mapper failure`() =
        runTest {
            val mockResponse = NetworkAssetResponse(data = emptyList())
            coEvery { assetsDataSource.getAssets() } returns Either.Right(mockResponse)
            coEvery { assetsMapper.map(input = emptyList()) } throws IllegalArgumentException("malformed asset payload")

            val error = runCatching { coinRepositoryImp.getAssets() }.exceptionOrNull()

            assertNotNull(error)
            error!!.message shouldBe "malformed asset payload"
        }
}
