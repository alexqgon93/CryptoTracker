package com.cryptotracker.ui.coins

import androidx.compose.runtime.Immutable

/**
 * Display-ready snapshot of a single coin row.
 *
 * Every numeric value is already formatted for the UI by [CoinUiMapper], except
 * [changePercent24Hr], which stays numeric so the screen can decide on the trend colour.
 *
 * @property rank Position by market cap, 1 being the largest.
 * @property price Current price as a currency string.
 * @property priceDifference24Hr Absolute price movement over the last 24 hours, as a currency string.
 */
@Immutable
data class CoinUIModel(
    val rank: Int,
    val id: String,
    val symbol: String,
    val name: String,
    val price: String,
    val priceDifference24Hr: String,
    val changePercent24Hr: Double,
)
