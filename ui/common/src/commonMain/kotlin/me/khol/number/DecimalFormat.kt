package me.khol.number

import java.util.*
import java.text.DecimalFormat as JavaDecimalFormat

private const val DEFAULT_ALLOW_NEGATIVE: Boolean = true
private const val DEFAULT_MAXIMUM_INTEGER_DIGITS: Int = Int.MAX_VALUE
private const val DEFAULT_MAXIMUM_FRACTION_DIGITS: Int = 2
private const val DEFAULT_MAXIMUM_LEADING_ZEROS: Int = 0
private const val DEFAULT_MAXIMUM_TRAILING_ZEROS: Int = Int.MAX_VALUE

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
    val allowNegative: Boolean = DEFAULT_ALLOW_NEGATIVE,
    val maximumIntegerDigits: Int = DEFAULT_MAXIMUM_INTEGER_DIGITS,
    val maximumFractionDigits: Int = DEFAULT_MAXIMUM_FRACTION_DIGITS,
    val maximumLeadingZeros: Int = DEFAULT_MAXIMUM_LEADING_ZEROS,
    val maximumTrailingZeros: Int = DEFAULT_MAXIMUM_TRAILING_ZEROS,
) {
    data class Symbols(
        val minusSign: Char,
        val decimalSeparator: Char,
        val groupingSeparator: Char,
        val zeroDigit: Char,
    )
}

fun DecimalFormat(
    locale: Locale,
    allowNegative: Boolean = DEFAULT_ALLOW_NEGATIVE,
    maximumIntegerDigits: Int = DEFAULT_MAXIMUM_INTEGER_DIGITS,
    maximumFractionDigits: Int = DEFAULT_MAXIMUM_FRACTION_DIGITS,
    maximumLeadingZeros: Int = DEFAULT_MAXIMUM_LEADING_ZEROS,
    maximumTrailingZeros: Int = DEFAULT_MAXIMUM_TRAILING_ZEROS,
): DecimalFormat {
    val javaFormat = JavaDecimalFormat.getNumberInstance(locale) as JavaDecimalFormat
    return DecimalFormat(
        symbols = DecimalFormat.Symbols(
            javaFormat.decimalFormatSymbols.minusSign,
            javaFormat.decimalFormatSymbols.decimalSeparator,
            javaFormat.decimalFormatSymbols.groupingSeparator,
            javaFormat.decimalFormatSymbols.zeroDigit,
        ),
        groupingSize = javaFormat.groupingSize,
        allowNegative = allowNegative,
        maximumIntegerDigits = maximumIntegerDigits,
        maximumFractionDigits = maximumFractionDigits,
        maximumLeadingZeros = maximumLeadingZeros,
        maximumTrailingZeros = maximumTrailingZeros,
    )
}
