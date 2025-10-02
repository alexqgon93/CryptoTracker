package com.bitpanda.livechallenge.network.api

import com.bitpanda.livechallenge.network.responses.NetworkAssetResponse
import com.bitpanda.livechallenge.network.responses.NetworkRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CryptoCoroutinesApi {
    @GET("assets")
    suspend fun getAsset(): Response<NetworkAssetResponse>

    @GET("rates")
    suspend fun getRates(): Response<NetworkRateResponse>
}
