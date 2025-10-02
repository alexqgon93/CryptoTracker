package com.bitpanda.livechallenge.network.source

import android.util.Log
import com.bitpanda.livechallenge.BuildConfig
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.network.di.WebSocketModule
import com.bitpanda.livechallenge.network.responses.WebSocketAssetsResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.serialization.deserialize
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import okio.IOException

class WebSocketDataSource @Inject constructor(
    @param:Named(WebSocketModule.Companion.WEBSOCKET_CLIENT) private val httpClient: HttpClient,
    @param:Named(WebSocketModule.Companion.WEBSOCKET_URL_NAME) private val websocketUrl: String
) {
    companion object Companion {
        const val TAG = "AssetsSocketDataSource"
        const val RETRY_DELAY = 10000L
        const val MAX_RETRIES = 5
    }

    private lateinit var webSocketSession: DefaultClientWebSocketSession

    fun connect(): Flow<List<Asset>> = flow {
        try {
            httpClient
                .webSocketSession {
                    url(websocketUrl)
                    parameter("apiKey", BuildConfig.api_key)
                }.apply {
                    webSocketSession = this
                }.incoming
                .receiveAsFlow()
                .collect { frame ->
                    try {
                        val message = webSocketSession.handleAsset(frame)?.toDomain()
                        if (message != null) {
                            emit(message)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error handling WebSocket frame", e)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to WebSocket", e)
        }
    }.retryWhen { cause, attempt ->
        if (cause is IOException && attempt < MAX_RETRIES) {
            delay(RETRY_DELAY)
            true
        } else {
            false
        }
    }.catch { e ->
        Log.e(TAG, "Error in WebSocket Flow", e)
    }

    suspend fun disconnect() = webSocketSession.close(
        reason =
            CloseReason(
                code = CloseReason.Codes.NORMAL,
                message = "Disconnect"
            )
    )

    private suspend fun DefaultClientWebSocketSession.handleAsset(
        frame: Frame
    ): WebSocketAssetsResponse? = when (frame) {
        is Frame.Text -> converter?.deserialize(content = frame)
        is Frame.Close -> {
            disconnect()
            null
        }

        else -> null
    }
}
