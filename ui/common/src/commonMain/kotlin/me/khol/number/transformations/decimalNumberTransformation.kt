package me.khol.number.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import me.khol.number.DecimalFormat

/**
 * The same as [decimalNumberTransformation] variant that accepts [AnnotatedString] except
 * that this variant accepts a plain [String] and discards information about offset mapping.
 */
fun decimalNumberTransformation(
    text: String,
    format: DecimalFormat,
): AnnotatedString = decimalNumberTransformation(
    annotatedString = AnnotatedString(text),
    format = format
).text

/**
 * Visual transformation that adds [DecimalFormat.Symbols.groupingSeparator] to
 * the [annotatedString].
 *
 * Note that this function is *not* responsible for limiting the number of digits or removing
 * non-numerical characters. This is merely for display purposes only. The number should be
 * previously validated by [validateDecimalNumber].
 *
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
// TODO: Convert to accept TransformedText?
fun decimalNumberTransformation(
    annotatedString: AnnotatedString,
    format: DecimalFormat,
): TransformedText {
    val text = annotatedString.text
    val minusSign = format.symbols.minusSign
    val decimalSeparator = format.symbols.decimalSeparator
    val groupingSeparator = format.symbols.groupingSeparator
    val groupingSize = format.groupingSize

    // Decided not to use `BigDecimal` and `decimalFormat.format(BigDecimal)` because:
    // * it struggles with displaying trailing decimal separator
    //   * it is displayed either always or never
    // * it struggles with displaying trailing zeros
    //   * they are displayed either always or never
    // * we have to calculate number of separators after a given point anyway

    val hasMinus = text.startsWith(minusSign)
    val integer = text.substringBefore(decimalSeparator).substringAfter(minusSign)
    val hasSeparator = text.contains(decimalSeparator)
    val fraction = text.substringAfter(decimalSeparator, missingDelimiterValue = "")

    val formattedInteger: String = integer
        .reversed()
        .chunked(groupingSize)
        .joinToString("$groupingSeparator")
        .reversed()

    val formattedFraction: String = fraction

    val formattedText: AnnotatedString = buildAnnotatedString {
        if (hasMinus) {
            append(minusSign)
        }
        append(formattedInteger)
        if (hasSeparator) {
            append(decimalSeparator)
            append(formattedFraction)
        }
    }

    val offsetMapping = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int {
            // Temporarily remove minus sign from further calculations
            val minusOffset = if (hasMinus) 1 else 0
            // Having a cursor placed before a full group will display the cursor
            // in front of the preceding grouping separator.
            // But since we do not show the leading grouping separator for the number
            // as a whole, this would jump to -1.
            if (offset == 0 || offset == minusOffset) return offset
            val offset = offset - minusOffset
            // Use -1 because we do not want to display a leading grouping separator.
            // I.e. we display 2 grouping separators for 7 digits but only 1 grouping
            // separator for 6 digits.
            val separators = (integer.length - 1) / groupingSize
            val integerOffset = offset.coerceAtMost(integer.length)
            val separatorsAfterOffset = (integer.length - integerOffset) / groupingSize
            return offset + separators - separatorsAfterOffset + minusOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            val allSeparators = formattedText.count { it == groupingSeparator }
            val separatorsAfterOffset = formattedText.drop(offset).count { it == groupingSeparator }
            return offset - allSeparators + separatorsAfterOffset
        }
    }

    return TransformedText(formattedText, offsetMapping)
}
