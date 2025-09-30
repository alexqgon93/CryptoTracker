package com.bitpanda.livechallenge.domain.models

data class Asset(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
)