package com.bitpanda.livechallenge.domain.models

data class Rate(
    val id: String,
    val symbol: String,
    val currencySymbol: String,
    val rateUsd: String,
    val type: String
)