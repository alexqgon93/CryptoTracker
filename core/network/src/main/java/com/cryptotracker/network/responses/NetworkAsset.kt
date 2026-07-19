package com.cryptotracker.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAsset(
    @param:Json(name = "id") val id: String,
    @param:Json(name = "name") val name: String,
    @param:Json(name = "rank") val rank: String,
    @param:Json(name = "symbol") val symbol: String,
    @param:Json(name = "priceUsd") val priceUsd: Double,
    @param:Json(name = "changePercent24Hr") val changePercent24Hr: Double,
)
