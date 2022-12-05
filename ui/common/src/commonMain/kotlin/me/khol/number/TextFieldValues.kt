package me.khol.number

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.math.min

/**
 * Replaces [regex] with [replacement] while preserving [TextFieldValue.selection] intact.
 *
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
fun TextFieldValue.replace(regex: Regex, replacement: String): TextFieldValue {
    val min = selection.min
    val max = selection.max
    val before = text.take(min)
    val selected = text.subSequence(min, max)
    val after = text.drop(max)

    val strippedBefore = before.replace(regex, replacement)
    val strippedSelected = selected.replace(regex, replacement)
    val strippedAfter = after.replace(regex, replacement)

    return TextFieldValue(
        text = buildString {
            append(strippedBefore)
            append(strippedSelected)
            append(strippedAfter)
        },
        selection = if (selection.reversed) {
            TextRange(
                start = strippedBefore.length + strippedSelected.length,
                end = strippedBefore.length
            )
        } else {
            TextRange(
                start = strippedBefore.length,
                end = strippedBefore.length + strippedSelected.length
            )
        }
    )
}

/**
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
fun TextFieldValue.dropLeadingZeros(
    maximumLeadingZeros: Int,
    symbols: DecimalFormat.Symbols,
): TextFieldValue {
    val leading = text.takeWhile { it == symbols.zeroDigit }
    val leadingCount = leading.length
    val dropCount = leadingCount - min(leadingCount, maximumLeadingZeros)
    var truncated = text.drop(dropCount)

    // TODO: Extract this to a separate function and replace [symbols] with [zeroDigit] only.
    truncated = if (truncated.isEmpty() || truncated.startsWith(symbols.decimalSeparator)) {
        "${symbols.zeroDigit}$truncated"
    } else {
        truncated
    }

    val droppedSize = text.length - truncated.length

    return TextFieldValue(
        text = truncated,
        selection = TextRange(
            (selection.start - droppedSize).coerceAtLeast(0),
            (selection.end - droppedSize).coerceAtLeast(0),
        )
    )
}

/**
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
fun TextFieldValue.dropTrailingZeros(
    maximumTrailingZeros: Int,
    symbols: DecimalFormat.Symbols,
): TextFieldValue {
    val trailing = text.takeLastWhile { it == symbols.zeroDigit }
    val trailingCount = trailing.length
    val dropCount = trailingCount - min(trailingCount, maximumTrailingZeros)
    val truncated = text.dropLast(dropCount)

    val droppedSize = text.length - truncated.length

    return TextFieldValue(
        text = truncated,
        selection = TextRange(
            (selection.start - droppedSize).coerceAtMost(truncated.length),
            (selection.end - droppedSize).coerceAtMost(truncated.length),
        )
    )
}

/**
 * Note: Does not preserve any [AnnotatedString.spanStyles] and [AnnotatedString.paragraphStyles].
 */
fun TextFieldValue.prepend(prefix: String): TextFieldValue {
    return TextFieldValue(
        text = "$prefix$text",
        selection = TextRange(
            selection.start + prefix.length,
            selection.end + prefix.length,
        )
    )
}
