package com.cryptotracker.network.di

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class NetworkModuleTest {
    @Test
    fun `GIVEN api key WHEN request executes THEN authorization bearer header is added`() {
        val request = executeAndCaptureRequest(apiKey = "test-key")

        assertEquals("Bearer test-key", request.header("Authorization"))
    }

    @Test
    fun `GIVEN blank api key WHEN request executes THEN authorization header is omitted`() {
        val request = executeAndCaptureRequest(apiKey = "")

        assertNull(request.header("Authorization"))
    }

    private fun executeAndCaptureRequest(apiKey: String): Request {
        lateinit var capturedRequest: Request
        val client =
            NetworkModule
                .createOkHttpClient(
                    apiKey = apiKey,
                    httpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE },
                ).newBuilder()
                .addInterceptor { chain ->
                    capturedRequest = chain.request()
                    Response
                        .Builder()
                        .request(capturedRequest)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body("OK".toResponseBody())
                        .build()
                }.build()

        client
            .newCall(
                Request
                    .Builder()
                    .url("https://example.test/assets")
                    .build(),
            ).execute()
            .close()

        return capturedRequest
    }
}
