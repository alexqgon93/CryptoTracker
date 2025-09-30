package com.bitpanda.livechallenge.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRateResponse(val `data`: List<NetworkRate>)
