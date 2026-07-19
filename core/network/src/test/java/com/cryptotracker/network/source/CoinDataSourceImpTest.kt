package com.cryptotracker.network.source

import arrow.core.Either
import com.cryptotracker.network.api.CryptoCoroutinesApi
import com.cryptotracker.network.responses.NetworkAssetResponse
import com.cryptotracker.network.responses.NetworkRateResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class CoinDataSourceImpTest {
    private val api: CryptoCoroutinesApi = mockk()
    private lateinit var dataSource: CoinDataSourceImp

    private val assetResponse = mockk<NetworkAssetResponse>()
    private val rateResponse = mockk<NetworkRateResponse>()

    @BeforeEach
    fun setUp() {
        dataSource = CoinDataSourceImp(cryptoApi = api)
    }

    @Test
    fun `GIVEN successful response WHEN getAssets THEN returns right`() =
        runTest {
            coEvery { api.getAsset() } returns Response.success(assetResponse)
            val result = dataSource.getAssets()
            assertTrue(result is Either.Right)
            coVerify(exactly = 1) { api.getAsset() }
        }

    @Test
    fun givenFailingAssetApi_whenGetAssets_thenReturnsLeft() =
        runTest {
            coEvery { api.getAsset() } throws RuntimeException("Server Error")
            val result = dataSource.getAssets()
            assertTrue(result is Either.Left)
            coVerify(exactly = 1) { api.getAsset() }
        }

    @Test
    fun givenSuccessfulRatesApi_whenGetRates_thenReturnsRight() =
        runTest {
            coEvery { api.getRates() } returns Response.success(rateResponse)
            val result = dataSource.getRates()
            assertTrue(result is Either.Right)
            coVerify(exactly = 1) { api.getRates() }
        }

    @Test
    fun givenFailingRatesApi_whenGetRates_thenReturnsLeft() =
        runTest {
            coEvery { api.getRates() } throws IllegalStateException("Failure")
            val result = dataSource.getRates()
            assertTrue(result is Either.Left)
            coVerify(exactly = 1) { api.getRates() }
        }
}
