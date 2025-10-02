package com.bitpanda.livechallenge.network.responses

import com.bitpanda.livechallenge.domain.models.Asset
import kotlinx.serialization.Serializable

@Serializable
class WebSocketAssetModel(
    val id: String,
    val name: String,
    val rank: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double
) {
    fun toDomain(): Asset = Asset(
        id = id,
        name = name,
        rank = rank.toInt(),
        symbol = symbol,
        price = priceUsd,
        changePercent24Hr = changePercent24Hr
    )
}
