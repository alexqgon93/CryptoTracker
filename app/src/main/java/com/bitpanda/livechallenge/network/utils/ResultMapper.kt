package com.bitpanda.livechallenge.network.utils

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}
