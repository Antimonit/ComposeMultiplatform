package me.khol.number

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Formats [newValue] as a decimal number and returns it.
 *
 * * Valid [newValue] that respects all restrictions imposed by [format] is returned as is.
 * * Some restrictions can be corrected automatically (removing unknown characters, stripping
 *   leading zeros, etc.) and will return the corrected value.
 * * Some restrictions can not be reasonably corrected automatically (multiple decimal separators,
 *   minus sign in the middle of the number, etc.) and will return the [oldValue].
 *
 * The returned [TextFieldValue.text] should be almost always parsable into a [BigDecimal], with
 * the exception of:
 * * empty string ("")
 * * solitary minus sign ("-")
 *
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
fun validateDecimalNumber(
    format: DecimalFormat,
    oldValue: TextFieldValue,
    newValue: TextFieldValue,
): TextFieldValue {

    // TODO: Are these two okay to use freely in regex?
    val minusSign = format.symbols.minusSign
    val decimalSeparator = format.symbols.decimalSeparator

    // Text did not change, only selection or composition has been updated.
    // We do not need to process any further.
    if (oldValue.text == newValue.text)
        return newValue

    // A special case.
    // If we delete leading digit so that only a decimal separator would
    // be left behind, we will delete the separator as well.
    if (oldValue.text.isNotEmpty() && newValue.text == "$decimalSeparator")
        return TextFieldValue(AnnotatedString(""))

    // Do not allow multiple decimal separators
    if (newValue.text.count { it == decimalSeparator } > 1)
        return oldValue

    // Do not allow multiple minus signs
    if (newValue.text.count { it == minusSign } > 1)
        return oldValue

    // Minus sign must not be in the middle of the number
    if (newValue.text.indexOfFirst { it == minusSign } > 0)
        return oldValue

    // Don't we need to escape the `decimalSeparator`?
    val regex = Regex("""[^\d${decimalSeparator}${minusSign}]""")
    val regexValue = newValue.replace(regex, "")
    val hasMinusSign = regexValue.text.startsWith("$minusSign")
    val minusPrefix = if (hasMinusSign) "$minusSign" else ""

    val cleanValue = if (hasMinusSign) {
        regexValue.replace(Regex("$minusSign"), "")
    } else {
        regexValue
    }

    if (cleanValue.text == ""/* || cleanValue.text == "$minusSign"*/)
        return cleanValue.prepend(minusPrefix)

    val strippedValue = cleanValue
        .dropLeadingZeros(format.maximumLeadingZeros, format.symbols)
        .dropTrailingZeros(format.maximumTrailingZeros, format.symbols)

    if (strippedValue.text.endsWith("$decimalSeparator"))
        return strippedValue.prepend(minusPrefix)

    try {
        val number = strippedValue.text.toBigDecimal()
        // Do not use `toString().length`, since that may include the minus sign
        val integerDigits = number.setScale(0, RoundingMode.DOWN).precision()
        val fractionDigits = number.scale()
        if (integerDigits > format.maximumIntegerDigits) return oldValue
        if (fractionDigits > format.maximumFractionDigits) return oldValue
        return strippedValue.prepend(minusPrefix)
    } catch (e: NumberFormatException) {
        // The requested value would not be a valid number, e.g.
        // * contains non-numeric characters
        // * contains multiple decimal separators
        // * contains minus sign somewhere else than at the start
        // * etc.
        // Simply disallow the update.
        println(e)
        return oldValue
    }
}
