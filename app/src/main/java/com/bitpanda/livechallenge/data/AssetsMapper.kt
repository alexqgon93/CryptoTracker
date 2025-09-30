package com.bitpanda.livechallenge.data

import com.bitpanda.livechallenge.data.remote.responses.NetworkAsset
import com.bitpanda.livechallenge.data.utils.ResultMapper
import com.bitpanda.livechallenge.domain.models.Asset
import javax.inject.Inject

class AssetsMapper @Inject constructor() : ResultMapper<List<NetworkAsset>, List<Asset>> {
    override fun map(input: List<NetworkAsset>): List<Asset> = input.map { it.toDomainModel() }

    private fun NetworkAsset.toDomainModel(): Asset = Asset(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr.toString().format("%.2f").toDouble(),
        rank = rank.toInt(),
    )
}