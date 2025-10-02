package com.bitpanda.livechallenge.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkError(@Json(name = "error") val error: String)
