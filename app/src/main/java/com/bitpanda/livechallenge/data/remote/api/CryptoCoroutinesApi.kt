package com.bitpanda.livechallenge.data.remote.api

import com.bitpanda.livechallenge.data.remote.responses.NetworkAssetResponse
import com.bitpanda.livechallenge.data.remote.responses.NetworkRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CryptoCoroutinesApi {

    @GET("assets")
    suspend fun getAsset(): Response<NetworkAssetResponse>

    @GET("rates")
    suspend fun getRates(): Response<NetworkRateResponse>
}