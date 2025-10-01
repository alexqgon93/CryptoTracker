package com.bitpanda.livechallenge.domain.usecases

import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

class RatesUseCaseTest {
    private val repository: CoinRepository = mockk()

    private lateinit var ratesUseCase: RatesUseCase

    @BeforeEach
    fun setUp() {
        ratesUseCase = RatesUseCase(repository = repository)
    }

    @Test
    fun `Given NetworkError WHEN call the use case THEN error is received`() = runTest {
        val exception = IOException("Network Error")
        coEvery { repository.getRates() } returns Either.Left(
            value = AppError.NetworkError(
                exception = exception
            )
        )
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.NetworkError(exception)))
    }

    @Test
    fun `Given BadRequest WHEN call the use case THEN error is received`() = runTest {
        val message = "Bad Request"
        coEvery { repository.getRates() } returns Either.Left(
            value = AppError.BadRequest(message = message)
        )
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.BadRequest(message)))
    }

    @Test
    fun `Given ServerError WHEN call the use case THEN error is received`() = runTest {
        val message = "ServerError Request"
        coEvery { repository.getRates() } returns Either.Left(
            value = AppError.ServerError(message = message)
        )
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.ServerError(message)))
    }

    @Test
    fun `Given Unknown WHEN call the use case THEN error is received`() = runTest {
        val message = "Unknown Error message"
        coEvery { repository.getRates() } returns Either.Left(
            value = AppError.Unknown(message = message)
        )
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.Unknown(message)))
    }

    @Test
    fun `Given Connectivity WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getRates() } returns Either.Left(value = AppError.Connectivity)
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.Connectivity))
    }

    @Test
    fun `Given Server WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getRates() } returns Either.Left(value = AppError.Server)
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.Server))
    }

    @Test
    fun `Given NoConnection WHEN call the use case THEN error is received Error`() = runTest {
        coEvery { repository.getRates() } returns Either.Left(value = AppError.NoConnectionError)
        assert(ratesUseCase.invoke() == Either.Left(value = AppError.NoConnectionError))
    }

    @Test
    fun `GIVEN a success WHEN call the use case THEN call the repository successfully`() = runTest {
        val response = mockk<Rate>()
        coEvery { repository.getRates() } returns Either.Right(value = listOf(response))
        assert(ratesUseCase.invoke() == Either.Right(value = listOf(response)))
    }
}