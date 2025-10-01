package com.bitpanda.livechallenge.ui

fun Double.format(digits: Int): Double = "%.${digits}f".format(this).toDouble()
fun Double.toEuroString(): String = "%,.2f €".format(this)
fun Double.formatToTwoDigits(): Double = this.format(2)