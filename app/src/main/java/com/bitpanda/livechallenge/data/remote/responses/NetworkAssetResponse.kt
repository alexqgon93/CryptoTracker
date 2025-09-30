package com.bitpanda.livechallenge.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAssetResponse(val `data`: List<NetworkAsset>)
