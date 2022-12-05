package me.khol.number

import java.util.*
import java.text.DecimalFormat as JavaDecimalFormat

/**
 * Similar to [java.text.DecimalFormat] but with limited features that are supported
 * by [validateDecimalNumber] and [decimalNumberTransformation].
 *
 * [symbols] commonly used separators and signs, may be locale-sensitive
 * [groupingSize] number of digits in between grouping separators
 * [maximumIntegerDigits] allowed number of digits before the decimal separator
 * [maximumFractionDigits] allowed number of digits after the decimal separator
 * [maximumLeadingZeros] allowed number of zeros in front of the integer part
 * [maximumTrailingZeros] allowed number of zeros at the end of the fraction part
 *
 * TODO: Add support for minimumIntegerDigits and minimumFractionDigits?
 */
data class DecimalFormat(
    val symbols: Symbols,
    val groupingSize: Int,
    val maximumIntegerDigits: Int = Int.MAX_VALUE,
    val maximumFractionDigits: Int = 2,
    val maximumLeadingZeros: Int = 0,
    val maximumTrailingZeros: Int = Int.MAX_VALUE,
) {

    data class Symbols(
        val minusSign: Char,
        val decimalSeparator: Char,
        val groupingSeparator: Char,
        val zeroDigit: Char,
    )

    constructor(locale: Locale) : this(
        javaFormat = JavaDecimalFormat.getNumberInstance(locale) as JavaDecimalFormat
    )

    constructor(javaFormat: JavaDecimalFormat) : this(
        symbols = Symbols(
            javaFormat.decimalFormatSymbols.minusSign,
            javaFormat.decimalFormatSymbols.decimalSeparator,
            javaFormat.decimalFormatSymbols.groupingSeparator,
            javaFormat.decimalFormatSymbols.zeroDigit,
        ),
        groupingSize = javaFormat.groupingSize,
    )
}

