package com.bitpanda.livechallenge.data.utils

import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.AppError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UtilsTest {

    @Test
    fun `GIVEN successful response WHEN safeCall is called THEN returns Right with body`() {
        val response = Response.success("data")
        val result = response.safeCall()
        assertTrue(result.isRight())
        assert("data" == (result as Either.Right).value)
    }

    @Test
    fun `GIVEN error response WHEN safeCall is called THEN returns Left with Failure`() {
        val errorBody = "{\"message\":\"error\"}".toResponseBody(null)
        val response = Response.error<String>(400, errorBody)
        val result = response.safeCall()
        assertTrue(result.isLeft())
    }

    @Test
    fun `GIVEN response with error body WHEN getErrorBody is called THEN returns NetworkError`() {
        val errorJson = "{\"error\":\"error\"}"
        val errorBody = errorJson.toResponseBody(null)
        val response = Response.error<String>(400, errorBody)
        val networkError = response.getErrorBody()
        assertNotNull(networkError)
    }

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
    fun `GIVEN IOException WHEN toError is called THEN returns Connectivity Failure`() =
        assert(Failure.Connectivity == IOException("network").toError())

    @Test
    fun `GIVEN HttpException WHEN toError is called THEN returns Server Failure with code`() {
        val response = Response.error<String>(
            500,
            "".toResponseBody("application/json".toMediaTypeOrNull())
        )
        val result = HttpException(response).toError()
        assertTrue(result is Failure.Server)
        assert(500 == (result as Failure.Server).code)
    }

    @Test
    fun `GIVEN unknown Throwable WHEN toError is called THEN returns Unknown Failure with message`() {
        val result = IllegalStateException("unexpected error").toError()
        assertTrue(result is Failure.Unknown)
        assert("unexpected error" == (result as Failure.Unknown).message)
    }

    @Test
    fun `GIVEN Failure Connectivity WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.Connectivity.toAppError() is AppError.Connectivity)

    @Test
    fun `GIVEN Failure NoConnectionError WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.NoConnectionError.toAppError() is AppError.NoConnectionError)


    @Test
    fun `GIVEN Failure ServerError WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.ServerError(500, "Server Error").toAppError()
        assertTrue(result is AppError.ServerError)
        assert("Server Error" == (result as AppError.ServerError).message)
    }

    @Test
    fun `GIVEN Failure BadRequest WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.BadRequest(500, "Server Error").toAppError()
        assertTrue(result is AppError.BadRequest)
        assert("Server Error" == (result as AppError.BadRequest).message)
    }

    @Test
    fun `GIVEN Failure Server WHEN toAppError is called THEN returns correct AppError`() =
        assertTrue(Failure.Server(500).toAppError() is AppError.Server)

    @Test
    fun `GIVEN Failure NetworkError WHEN toAppError is called THEN returns correct AppError`() {
        val result = Failure.NetworkError(IOException("Server Error")).toAppError()
        assertTrue(result is AppError.NetworkError)
        assert("Server Error" == (result as AppError.NetworkError).exception.message)
    }

}