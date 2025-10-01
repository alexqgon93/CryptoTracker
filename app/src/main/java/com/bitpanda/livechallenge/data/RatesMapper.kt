package com.bitpanda.livechallenge.data

import com.bitpanda.livechallenge.data.remote.responses.NetworkRate
import com.bitpanda.livechallenge.data.utils.ResultMapper
import com.bitpanda.livechallenge.domain.models.Rate
import javax.inject.Inject

class RatesMapper @Inject constructor() : ResultMapper<List<NetworkRate>, List<Rate>> {
    override fun map(input: List<NetworkRate>): List<Rate> = input.map { it.toDomainModel() }

    private fun NetworkRate.toDomainModel(): Rate = Rate(
        id = id,
        symbol = symbol,
        currencySymbol = currencySymbol,
        rateUsd = rateUsd,
        type = type
    )
}
