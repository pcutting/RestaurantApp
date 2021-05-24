package com.philipcutting.restaurantapp.utilities

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.toCurrencyFormatFromDouble(): String {
    val formatter = DecimalFormat("#0.${"".padEnd(2, '0')}")
    val partiallyFormattedValue =this.toBigDecimal()
        .setScale(2, RoundingMode.HALF_UP).toDouble()
    return "$${formatter.format(partiallyFormattedValue)}"
}