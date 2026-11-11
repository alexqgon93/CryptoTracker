package com.cryptotracker.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRate(
    @param:Json(name = "id") val id: String,
    @param:Json(name = "symbol") val symbol: String,
    @param:Json(name = "currencySymbol") val currencySymbol: String?,
    @param:Json(name = "rateUsd") val rateUsd: String,
    @param:Json(name = "type") val type: String,
)
