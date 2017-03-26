package com.example.pepah.fakekb

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Created by pepah on 25.03.2017.
 */


fun BigDecimal.formatCurrency(currencyCode: String?): String {
    return this.formatCurrency(currencyCode, DecimalFormat("#.##"))
}

fun BigDecimal.formatCurrencyRounded(currencyCode: String?): String {
    if (this.abs().smallerThan(BigDecimal(100))) {
        return this.formatCurrency(currencyCode, DecimalFormat("#.##"))
    } else {
        return this.formatCurrency(currencyCode, DecimalFormat("#"))
    }
}

fun BigDecimal.formatCurrency(currencyCode: String?, numberFormat: DecimalFormat): String {
    when (currencyCode) {
        "CZK" -> {
            return this.formatNumber(numberFormat) + " Kč"
        }
        "USD" -> {
            if (this.smallerThan(BigDecimal.ZERO)) {
                return "-$" + this.abs().formatNumber(numberFormat)
            } else {
                return "$" + this.formatNumber(numberFormat)
            }
        }
        else -> {
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance(currencyCode)
            return this.formatNumber(format)
        }
    }
}

fun BigDecimal.formatNumber(formatter: NumberFormat): String {
    return formatter.format(this.toDouble()).removeTrailingZeros()
}

fun BigDecimal.formatWeight(): String {
    return this.formatNumber(DecimalFormat("#.##"))
}

fun BigDecimal.formatAmount(): String {
    if (this.abs().smallerThan(BigDecimal(100))) {
        return this.formatNumber(DecimalFormat("#.##"))
    } else {
        return this.formatNumber(DecimalFormat("#"))
    }
}

/**
 * When displayed, it's always > 1
 * Example 1: CZK->USD: 21.7 is displayed as 21.7 CZK = 1 USD
 * Example 2: EUR->USD: 0.91 is displayed as 1.1 USD = 1 EUR
 */
fun BigDecimal.formatExchangeRate(): String {
    if (this.largerThan(BigDecimal.ONE)) {
        return this.formatNumber(DecimalFormat("#.#####"))
    } else {
        return BigDecimal.ONE.divideWithScale(this).formatNumber(DecimalFormat("#.#####"))
    }
}

fun Locale.currency(): String {
    return Currency.getInstance(this).currencyCode

}


fun BigDecimal.largerThan(number: BigDecimal): Boolean {
    return this > number
}

fun BigDecimal.smallerThan(number: BigDecimal): Boolean {
    return this < number
}

fun BigDecimal.smallerOrEqualThan(number: BigDecimal): Boolean {
    return this <= number
}

fun BigDecimal.isNegative(): Boolean {
    return this < BigDecimal.ZERO
}

fun BigDecimal.divideWithScale(number: BigDecimal): BigDecimal {
    return divide(number, 20, RoundingMode.HALF_UP)
}

fun MutableMap<String, BigDecimal>.add(entry: Pair<String, BigDecimal>) {
    val amount = this[entry.first]
    if (amount == null) {
        this[entry.first] = entry.second
    } else {
        this[entry.first] = amount.add(entry.second)
    }
}

fun MutableMap<String, BigDecimal>.subtract(entry: Pair<String, BigDecimal>) {
    val amount = this[entry.first]
    if (amount == null) {
        this[entry.first] = entry.second.negate()
    } else {
        this[entry.first] = amount.subtract(entry.second)
    }
}

fun String.canBeConvertedToDouble(): Boolean {
    try {
        this.toDouble()
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}

fun String.removeTrailingZeros(): String {
    return if (!this.contains(".")) this else this.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
}

fun String.toBigDecimal(): BigDecimal {
    return BigDecimal(this)
}

fun List<BigDecimal>.sum(): BigDecimal {
    return this.fold(BigDecimal.ZERO, BigDecimal::add)
}

fun List<BigDecimal>.gcd(): BigDecimal {
    if (this.isEmpty()) {
        return BigDecimal.ZERO
    }
    var gcd = this.first()
    for (i in 1..this.size - 1) {
        gcd = gcd(gcd, this[i])
    }
    return gcd
}

fun BigDecimal.isZero(): Boolean {
    return this.toDouble() == 0.0
}

fun <E> List<E>.randomElement(): E {
    return this[Random().nextInt(this.size)]
}

/**
 * Calculates Greatest Common Divisor using Euclidean Algorithm.
 */
private fun gcd(a: BigDecimal, b: BigDecimal): BigDecimal {
    if (b.toDouble() == 0.0) return a
    return gcd(b, a % b)
}

