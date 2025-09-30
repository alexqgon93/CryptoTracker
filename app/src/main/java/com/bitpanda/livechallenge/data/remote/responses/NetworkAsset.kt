package com.bitpanda.livechallenge.data.remote.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAsset(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "rank") val rank: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "priceUsd") val priceUsd: Double,
    @Json(name = "changePercent24Hr") val changePercent24Hr: Double,
)