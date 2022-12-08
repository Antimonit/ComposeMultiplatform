package me.khol.number

import androidx.compose.ui.text.AnnotatedString
import me.khol.number.transformations.decimalNumberTransformation
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class DecimalNumberTransformationTest {

    @Test
    fun `check transformedText is the same as java-util-DecimalFormat-format`() {

    }

    @Test
    fun `check TransformedText adds separators correctly`() {
        fun filter(originalText: String): String {
            return decimalNumberTransformation(
                annotatedString = AnnotatedString(originalText),
                format = DecimalFormat(Locale.US),
            ).text.text
        }
        assertEquals("", filter(""))
        assertEquals("1", filter("1"))
        assertEquals("12", filter("12"))
        assertEquals("123", filter("123"))
        assertEquals("1,234", filter("1234"))
        assertEquals("12,345", filter("12345"))
        assertEquals("123,456", filter("123456"))
        assertEquals("1,234,567", filter("1234567"))

        assertEquals(".", filter("."))
        assertEquals(".0", filter(".0"))
        assertEquals(".00", filter(".00"))
        assertEquals(".000", filter(".000"))
        assertEquals(".0000", filter(".0000"))
        assertEquals(".1", filter(".1"))
        assertEquals(".12", filter(".12"))
        assertEquals(".123", filter(".123"))
        assertEquals(".1234", filter(".1234"))

        assertEquals("0.", filter("0."))
        assertEquals("0.0", filter("0.0"))
        assertEquals("0.00", filter("0.00"))
        assertEquals("0.000", filter("0.000"))
        assertEquals("0.0000", filter("0.0000"))
        assertEquals("0.1", filter("0.1"))
        assertEquals("0.12", filter("0.12"))
        assertEquals("0.123", filter("0.123"))
        assertEquals("0.1234", filter("0.1234"))

        assertEquals("0,000.0000", filter("0000.0000"))
        assertEquals("1,234.1234", filter("1234.1234"))
    }

    @Test
    fun `check mapping to and from TransformedText`() {
        val originalText = "1234567.1234"
        val transformedText = decimalNumberTransformation(
            annotatedString = AnnotatedString(originalText),
            format = DecimalFormat(Locale.US),
        )

        val text = transformedText.text
        val mapping = transformedText.offsetMapping

        val expected = "1,234,567.1234"
        assertEquals(expected, text.text)

        mapOf(
            0 to 0,   // [ | 1234567.1234] -> [ | 1,234,567.1234]
            1 to 1,   // [1 | 234567.1234] -> [1 | ,234,567.1234]
            2 to 3,   // [12 | 34567.1234] -> [1,2 | 34,567.1234]
            3 to 4,   // [123 | 4567.1234] -> [1,23 | 4,567.1234]
            4 to 5,   // [1234 | 567.1234] -> [1,234 | ,567.1234]
            5 to 7,   // [12345 | 67.1234] -> [1,234,5 | 67.1234]
            6 to 8,   // [123456 | 7.1234] -> [1,234,56 | 7.1234]
            7 to 9,   // [1234567 | .1234] -> [1,234,567 | .1234]
            8 to 10,  // [1234567. | 1234] -> [1,234,567. | 1234]
            9 to 11,  // [1234567.1 | 234] -> [1,234,567.1 | 234]
            10 to 12, // [1234567.12 | 34] -> [1,234,567.12 | 34]
            11 to 13, // [1234567.123 | 4] -> [1,234,567.123 | 4]
            12 to 14, // [1234567.1234 | ] -> [1,234,567.1234 | ]
        ).forEach { (from, to) ->
            assertEquals(to, mapping.originalToTransformed(from))
        }

        mapOf(
            0 to 0,   // [ | 1,234,567.1234] -> [ | 1234567.1234]
            1 to 1,   // [1 | ,234,567.1234] -> [1 | 234567.1234]
            2 to 1,   // [1, | 234,567.1234] -> [1 | 234567.1234]
            3 to 2,   // [1,2 | 34,567.1234] -> [12 | 34567.1234]
            4 to 3,   // [1,23 | 4,567.1234] -> [123 | 4567.1234]
            5 to 4,   // [1,234 | ,567.1234] -> [1234 | 567.1234]
            6 to 4,   // [1,234, | 567.1234] -> [1234 | 567.1234]
            7 to 5,   // [1,234,5 | 67.1234] -> [12345 | 67.1234]
            8 to 6,   // [1,234,56 | 7.1234] -> [123456 | 7.1234]
            9 to 7,   // [1,234,567 | .1234] -> [1234567 | .1234]
            10 to 8,  // [1,234,567. | 1234] -> [1234567. | 1234]
            11 to 9,  // [1,234,567.1 | 234] -> [1234567.1 | 234]
            12 to 10, // [1,234,567.12 | 34] -> [1234567.12 | 34]
            13 to 11, // [1,234,567.123 | 4] -> [1234567.123 | 4]
            14 to 12, // [1,234,567.1234 | ] -> [1234567.1234 | ]
        ).forEach { (from, to) ->
            assertEquals(to, mapping.transformedToOriginal(from))
        }

        repeat(originalText.length + 1) { original ->
            val transformed = mapping.originalToTransformed(original)
            val newOriginal = mapping.transformedToOriginal(transformed)
            assertEquals(original, newOriginal)
        }
    }
}
