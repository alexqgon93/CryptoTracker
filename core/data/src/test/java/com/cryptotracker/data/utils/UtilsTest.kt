package com.cryptotracker.data.utils

import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.network.utils.Failure
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class UtilsTest {
    @Test
    fun `GIVEN Either Left WHEN mapResult is called THEN returns Left AppError`() {
        val either: Either<Failure, String> = Either.Left(Failure.Connectivity)
        val result = either.mapResult { it.length }
        assertTrue(result.isLeft())
        assertTrue((result as Either.Left).value is AppError.Connectivity)
    }

    @Test
    fun `GIVEN Either Right WHEN mapResult is called THEN returns Right transformed value`() {
        val either: Either<Failure, String> = Either.Right("test")
        val result = either.mapResult { it.length }
        assertTrue(result.isRight())
        assert(4 == (result as Either.Right).value)
    }

    @Test
    fun `GIVEN Failure Connectivity WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.Connectivity.toAppError() is AppError.Connectivity)

    @Test
    fun `GIVEN NoConnectionError WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.NoConnectionError.toAppError() is AppError.NoConnectionError)

    @Test
    fun `GIVEN Failure ServerError WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.ServerError(500, "Server Error").toAppError()
        assertTrue(result is AppError.ServerError)
        assert("Server Error" == (result as AppError.ServerError).message)
    }

    @Test
    fun `GIVEN Failure BadRequest WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.BadRequest(500, "BadRequest Error").toAppError()
        assertTrue(result is AppError.BadRequest)
        assert("BadRequest Error" == (result as AppError.BadRequest).message)
    }

    @Test
    fun `GIVEN Failure Unknown WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.Unknown("unexpected failure").toAppError()
        assertTrue(result is AppError.Unknown)
        assert("unexpected failure" == (result as AppError.Unknown).message)
    }

    @Test
    fun `GIVEN Failure Server WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.Server(500).toAppError() is AppError.Server)

    @Test
    fun `GIVEN Failure NetworkError WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.NetworkError(IOException("NetworkError Error")).toAppError()
        assertTrue(result is AppError.NetworkError)
        assert("NetworkError Error" == (result as AppError.NetworkError).exception.message)
    }
}
