import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.withStyle

private const val sep = "    "
private const val separatorSize = sep.length
private const val mask = "1234${sep}5678${sep}1234${sep}5678"

fun creditCardFilter(text: AnnotatedString): TransformedText {
    val trimmed = text.text.take(16)

    val annotatedString = buildAnnotatedString {
        trimmed.forEachIndexed { index, c ->
            append(c)
            if (index % 4 == 3 && index != 15) {
                append(sep)
            }
        }
        withStyle(SpanStyle(color = Color.LightGray)) {
            append(mask.drop(length))
        }
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val separatorCount = when {
                offset < 4 -> 0
                offset < 8 -> 1
                offset < 12 -> 2
                offset < 16 -> 3
                else -> 3
            }
            return offset.coerceAtMost(16) + separatorCount * separatorSize
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset <= 4 + 0 * separatorSize -> offset - 0 * separatorSize
                offset <= 8 + 1 * separatorSize -> offset - 1 * separatorSize
                offset <= 12 + 2 * separatorSize -> offset - 2 * separatorSize
                offset <= 16 + 3 * separatorSize -> offset - 3 * separatorSize
                else -> 16
            }.coerceAtMost(text.length)
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}