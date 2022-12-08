package me.khol.number.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

/**
 * Empty text will be displayed as empty text, so that the placeholder
 * can be displayed.
 */
fun emptyTextTransformation(
    annotatedString: AnnotatedString,
    nextTransformation: (AnnotatedString) -> TransformedText,
): TransformedText =
    if (annotatedString.text.isEmpty()) {
        TransformedText(annotatedString, OffsetMapping.Identity)
    } else {
        nextTransformation(annotatedString)
    }
