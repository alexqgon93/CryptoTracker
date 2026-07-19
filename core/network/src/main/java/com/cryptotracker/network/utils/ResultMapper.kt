package com.cryptotracker.network.utils

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}
