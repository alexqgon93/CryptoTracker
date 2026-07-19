package com.cryptotracker.data.mappers

import com.cryptotracker.domain.models.Asset
import com.cryptotracker.network.responses.NetworkAsset
import com.cryptotracker.network.utils.ResultMapper
import javax.inject.Inject

class AssetsMapper
    @Inject
    constructor() : ResultMapper<List<NetworkAsset>, List<Asset>> {
        override fun map(input: List<NetworkAsset>): List<Asset> = input.map { it.toDomainModel() }

        private fun NetworkAsset.toDomainModel(): Asset =
            Asset(
                id = id,
                name = name,
                symbol = symbol,
                price = priceUsd,
                changePercent24Hr = changePercent24Hr,
                rank = rank.toInt(),
            )
    }
