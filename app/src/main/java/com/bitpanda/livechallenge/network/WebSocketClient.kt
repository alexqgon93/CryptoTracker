package com.bitpanda.livechallenge.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val jsonConfig =
    Json {
        ignoreUnknownKeys = true
    }

object WebSocketClient {
    val client =
        HttpClient(engineFactory = OkHttp) {
            install(plugin = WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(format = Json)
            }
        }
}

object RestClient {
    val client =
        HttpClient {
            install(plugin = ContentNegotiation) {
                json(jsonConfig)
            }
        }
}
