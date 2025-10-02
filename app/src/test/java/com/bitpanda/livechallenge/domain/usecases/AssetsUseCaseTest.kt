package com.bitpanda.livechallenge.domain.usecases

import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AssetsUseCaseTest {
    private val repository: CoinRepository = mockk()
    private lateinit var assetsUseCase: AssetsUseCase

    @BeforeEach
    fun setUp() {
        assetsUseCase = AssetsUseCase(repository = repository)
    }

    @Test
    fun `Given NetworkError WHEN call the use case THEN error is received`() = runTest {
        val exception = IOException("Network Error")
        coEvery { repository.getAssets() } returns
            Either.Left(
                value =
                AppError.NetworkError(
                    exception = exception
                )
            )
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.NetworkError(exception)))
    }

    @Test
    fun `Given BadRequest WHEN call the use case THEN error is received`() = runTest {
        val message = "Bad Request"
        coEvery { repository.getAssets() } returns
            Either.Left(
                value = AppError.BadRequest(message = message)
            )
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.BadRequest(message)))
    }

    @Test
    fun `Given ServerError WHEN call the use case THEN error is received`() = runTest {
        val message = "ServerError Request"
        coEvery { repository.getAssets() } returns
            Either.Left(
                value = AppError.ServerError(message = message)
            )
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.ServerError(message)))
    }

    @Test
    fun `Given Unknown WHEN call the use case THEN error is received`() = runTest {
        val message = "Unknown Error message"
        coEvery { repository.getAssets() } returns
            Either.Left(
                value = AppError.Unknown(message = message)
            )
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.Unknown(message)))
    }

    @Test
    fun `Given Connectivity WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getAssets() } returns Either.Left(value = AppError.Connectivity)
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.Connectivity))
    }

    @Test
    fun `Given Server WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getAssets() } returns Either.Left(value = AppError.Server)
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.Server))
    }

    @Test
    fun `Given NoConnection WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getAssets() } returns
            Either.Left(value = AppError.NoConnectionError)
        assert(assetsUseCase.invoke() == Either.Left(value = AppError.NoConnectionError))
    }

    @Test
    fun `GIVEN a success WHEN call the use case THEN call the repository successfully`() = runTest {
        val response = mockk<Asset>()
        coEvery { repository.getAssets() } returns Either.Right(value = listOf(response))
        assert(assetsUseCase.invoke() == Either.Right(value = listOf(response)))
    }
}
