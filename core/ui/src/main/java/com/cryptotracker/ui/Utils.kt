package com.cryptotracker.ui

import java.text.NumberFormat
import java.util.Locale
import kotlin.text.format

fun Double.toCorrectCurrencyForm(): String = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(this)

fun Double.formatToTwoDigits(): Double = "%.${2}f".format(this).toDouble()

fun Double.formatToEuropean(): String =
    NumberFormat
        .getNumberInstance(Locale.GERMANY)
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(this)
