package me.khol.number.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

// TODO: This won't work well for a placeholder.
//  We must show the cursor after the [prefix], not before.
fun affixTransformation(
    prefix: String,
    suffix: String,
    text: AnnotatedString,
): AnnotatedString = affixTransformation(
    prefix = prefix,
    suffix = suffix,
    text = TransformedText(text, OffsetMapping.Identity)
).text

fun affixTransformation(
    prefix: String,
    suffix: String,
    text: TransformedText,
) = TransformedText(
    text = AnnotatedString(prefix) + text.text + AnnotatedString(suffix),
    offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            text.offsetMapping.originalToTransformed(offset) + prefix.length

        override fun transformedToOriginal(offset: Int): Int =
            (text.offsetMapping.transformedToOriginal(offset) - prefix.length)
                .coerceAtMost(text.text.length)
    }
)
