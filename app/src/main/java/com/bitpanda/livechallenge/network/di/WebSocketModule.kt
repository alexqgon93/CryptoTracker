package com.bitpanda.livechallenge.network.di

import com.bitpanda.livechallenge.network.RestClient
import com.bitpanda.livechallenge.network.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class WebSocketModule {
    companion object Companion {
        const val WEBSOCKET_URL = "wss://wss.coincap.io/prices?assets=ALL"
        const val WEBSOCKET_URL_NAME = "WEBSOCKET_URL"
        const val WEBSOCKET_CLIENT = "WEBSOCKET_CLIENT"
        const val API_CLIENT = "API_CLIENT"
    }

    @Provides
    @Named(WEBSOCKET_CLIENT)
    fun providesWebsocketHttpClient(): HttpClient = WebSocketClient.client

    @Provides
    @Named(WEBSOCKET_URL_NAME)
    fun providesWebsocketURL(): String = WEBSOCKET_URL

    @Provides
    @Named(API_CLIENT)
    fun providesAPIHttpClient(): HttpClient = RestClient.client
}
