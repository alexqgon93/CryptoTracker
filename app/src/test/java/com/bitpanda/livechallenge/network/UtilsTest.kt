package com.bitpanda.livechallenge.network

import arrow.core.Either
import com.bitpanda.livechallenge.network.utils.Failure
import com.bitpanda.livechallenge.network.utils.getErrorBody
import com.bitpanda.livechallenge.network.utils.safeCall
import com.bitpanda.livechallenge.network.utils.toError
import com.bitpanda.livechallenge.network.utils.tryCall
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class UtilsTest {
    @Test
    fun `GIVEN successful response WHEN tryCall is called THEN returns Right with body`() =
        runTest {
            val result = tryCall { Response.success("data") }
            assertTrue(result.isRight())
            assertEquals("data", (result as Either.Right).value)
        }

    @Test
    fun `GIVEN error response WHEN tryCall is called THEN returns Left Failure`() = runTest {
        val errorBody = "{\"error\":\"error\"}".toResponseBody(null)
        val result = tryCall { Response.error<String>(400, errorBody) }
        assertTrue(result.isLeft())
    }

    @Test
    fun `GIVEN IOException thrown WHEN tryCall is called THEN returns Left Connectivity Failure`() =
        runTest {
            val result = tryCall<String> { throw IOException("network") }
            assertTrue(result.isLeft())
            assertEquals(Failure.Connectivity, (result as Either.Left).value)
        }

    @Test
    fun `GIVEN HttpException thrown WHEN tryCall is called THEN returns Left Server Failure`() =
        runTest {
            val response = Response.error<String>(500, "".toResponseBody(null))
            val result = tryCall<String> { throw HttpException(response) }
            assertTrue(result.isLeft())
            assertTrue((result as Either.Left).value is Failure.Server)
            assertEquals(500, (result.value as Failure.Server).code)
        }

    @Test
    fun `GIVEN unknown exception WHEN tryCall is called THEN returns Left Unknown Failure`() =
        runTest {
            val result = tryCall<String> { throw IllegalStateException("unexpected") }
            assertTrue(result.isLeft())
            assertTrue((result as Either.Left).value is Failure.Unknown)
            assertEquals("unexpected", (result.value as Failure.Unknown).message)
        }

    @Test
    fun `GIVEN response with null body WHEN safeCall is called THEN returns Left with Failure`() {
        val response = Response.success<String>(null)
        val result = response.safeCall()
        assertTrue(result.isLeft())
    }

    @Test
    fun `GIVEN response WHEN safeCall is called THEN returns Right with body`() {
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
    fun `GIVEN IOException WHEN toError is called THEN returns Connectivity Failure`() =
        assert(Failure.Connectivity == IOException("network").toError())

    @Test
    fun `GIVEN HttpException WHEN toError is called THEN returns Server Failure with code`() {
        val response =
            Response.error<String>(
                500,
                "".toResponseBody("application/json".toMediaTypeOrNull())
            )
        val result = HttpException(response).toError()
        assertTrue(result is Failure.Server)
        assert(500 == (result as Failure.Server).code)
    }

    @Test
    fun `GIVEN unknown Throwable WHEN toError() THEN returns Unknown Failure with message`() {
        val result = IllegalStateException("unexpected error").toError()
        assertTrue(result is Failure.Unknown)
        assert("unexpected error" == (result as Failure.Unknown).message)
    }
}
