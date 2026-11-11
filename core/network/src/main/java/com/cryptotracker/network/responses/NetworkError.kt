package com.cryptotracker.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkError(
    @param:Json(name = "error") val error: String,
)
