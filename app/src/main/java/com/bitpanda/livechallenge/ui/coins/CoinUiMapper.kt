package com.bitpanda.livechallenge.ui.coins

import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.ui.formatToTwoDigits
import com.bitpanda.livechallenge.ui.toEuroString
import javax.inject.Inject

class CoinUiMapper @Inject constructor() {

    fun map(coins: List<Asset>): List<CoinUIModel> = coins.sortedBy { it.rank }.map { asset ->
        CoinUIModel(
            id = asset.id,
            name = asset.name,
            symbol = asset.symbol,
            price = asset.price.toEuroString(),
            changePercent24Hr = asset.changePercent24Hr.formatToTwoDigits(),
            rank = asset.rank,
            priceDifference24Hr = asset.priceDifference24Hr(changePercent = asset.changePercent24Hr)
        )
    }
}
