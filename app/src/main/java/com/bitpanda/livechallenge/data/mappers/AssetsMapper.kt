package com.bitpanda.livechallenge.data.mappers

import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.network.responses.NetworkAsset
import com.bitpanda.livechallenge.network.utils.ResultMapper
import javax.inject.Inject

class AssetsMapper
@Inject
constructor() : ResultMapper<List<NetworkAsset>, List<Asset>> {
    override fun map(input: List<NetworkAsset>): List<Asset> = input.map { it.toDomainModel() }

    private fun NetworkAsset.toDomainModel(): Asset = Asset(
        id = id,
        name = name,
        symbol = symbol,
        price = priceUsd,
        changePercent24Hr = changePercent24Hr,
        rank = rank.toInt()
    )
}
