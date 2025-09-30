package com.bitpanda.livechallenge.data.remote.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRate(
    @Json(name = "id") val id: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "currencySymbol") val currencySymbol: String,
    @Json(name = "rateUsd") val rateUsd: String,
    @Json(name = "type") val type: String
)