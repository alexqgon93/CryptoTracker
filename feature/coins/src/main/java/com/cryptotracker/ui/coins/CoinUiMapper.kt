package com.cryptotracker.ui.coins

import com.cryptotracker.domain.models.Asset
import com.cryptotracker.ui.formatToTwoDigits
import com.cryptotracker.ui.toCorrectCurrencyForm
import javax.inject.Inject

class CoinUiMapper
    @Inject
    constructor() {
        fun map(coins: List<Asset>): List<CoinUIModel> =
            coins.sortedBy { it.rank }.map { asset ->
                CoinUIModel(
                    id = asset.id,
                    name = asset.name,
                    symbol = asset.symbol,
                    price = asset.price.toCorrectCurrencyForm(),
                    changePercent24Hr = asset.changePercent24Hr.formatToTwoDigits(),
                    rank = asset.rank,
                    priceDifference24Hr =
                        asset
                            .priceDifference24Hr(
                                changePercent = asset.changePercent24Hr,
                            ).formatToTwoDigits()
                            .toCorrectCurrencyForm(),
                )
            }
    }
