package com.bitpanda.livechallenge.data.utils

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}