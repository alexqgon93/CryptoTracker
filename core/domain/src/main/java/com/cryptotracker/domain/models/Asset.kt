package com.cryptotracker.domain.models

data class Asset(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val price: Double,
    val changePercent24Hr: Double,
) {
    fun priceDifference24Hr(changePercent: Double): Double = price * (changePercent / 100)
}
