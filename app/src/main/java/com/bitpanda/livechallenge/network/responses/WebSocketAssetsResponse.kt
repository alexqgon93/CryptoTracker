package com.bitpanda.livechallenge.network.responses

import com.bitpanda.livechallenge.domain.models.Asset
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketAssetsResponse(val data: List<WebSocketAssetModel>) {
    fun toDomain(): List<Asset> = data.map { it.toDomain() }
}
