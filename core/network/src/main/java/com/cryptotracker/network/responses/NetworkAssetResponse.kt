package com.cryptotracker.network.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAssetResponse(
    val `data`: List<NetworkAsset>,
)
