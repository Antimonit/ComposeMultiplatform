package me.khol.number.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import me.khol.number.DecimalFormat

fun fixedZeroTransformation(
    string: String,
    format: DecimalFormat,
    fixedZeros: Int,
): AnnotatedString = fixedZeroTransformation(
    annotatedString = AnnotatedString(string),
    format = format,
    fixedZeros = fixedZeros,
).text

/**
 * Always display [fixedZeros] number of zeros at the end of the [AnnotatedString].
 *
 * These suffixed zeros are not selectable since they are just part of [VisualTransformation]
 */
fun fixedZeroTransformation(
    annotatedString: AnnotatedString,
    format: DecimalFormat,
    fixedZeros: Int,
): TransformedText {
    val transformation = decimalNumberTransformation(
        annotatedString = AnnotatedString(
            annotatedString.text + format.symbols.zeroDigit.toString().repeat(fixedZeros),
        ),
        format = format,
    )

    return TransformedText(
        text = transformation.text,
        offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                transformation.offsetMapping.originalToTransformed(offset)

            override fun transformedToOriginal(offset: Int): Int =
                transformation.offsetMapping.transformedToOriginal(offset)
                    .coerceAtMost(annotatedString.length)
        }
    )
}
