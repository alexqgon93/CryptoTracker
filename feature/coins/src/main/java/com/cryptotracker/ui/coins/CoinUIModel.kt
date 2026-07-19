package com.cryptotracker.ui.coins

data class CoinUIModel(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val price: String,
    val changePercent24Hr: Double,
    val priceDifference24Hr: String,
)
