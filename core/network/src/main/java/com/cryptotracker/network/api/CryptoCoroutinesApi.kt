package com.cryptotracker.network.api

import com.cryptotracker.network.responses.NetworkAssetResponse
import com.cryptotracker.network.responses.NetworkRateResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit definition of the CoinCap REST endpoints used by the app.
 *
 * Both calls return the raw [Response] so callers can map HTTP failures themselves.
 */
interface CryptoCoroutinesApi {
    /** Fetches the market overview of all tracked assets. */
    @GET("assets")
    suspend fun getAsset(): Response<NetworkAssetResponse>

    /** Fetches the conversion rates of the supported currencies. */
    @GET("rates")
    suspend fun getRates(): Response<NetworkRateResponse>
}
