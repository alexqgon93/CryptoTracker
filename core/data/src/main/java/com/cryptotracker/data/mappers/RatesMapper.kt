package com.cryptotracker.data.mappers

import com.cryptotracker.domain.models.Rate
import com.cryptotracker.network.responses.NetworkRate
import com.cryptotracker.network.utils.ResultMapper
import javax.inject.Inject

class RatesMapper
    @Inject
    constructor() : ResultMapper<List<NetworkRate>, List<Rate>> {
        override fun map(input: List<NetworkRate>): List<Rate> = input.map { it.toDomainModel() }

        private fun NetworkRate.toDomainModel(): Rate =
            Rate(
                id = id,
                symbol = symbol,
                currencySymbol = currencySymbol,
                rateUsd = rateUsd,
                type = type,
            )
    }
