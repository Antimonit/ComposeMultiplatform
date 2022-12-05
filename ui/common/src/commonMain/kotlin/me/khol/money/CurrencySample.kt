package me.khol.money

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun CurrencySample() {
    var text by remember { mutableStateOf("") }

    Column {
        var locale by remember { mutableStateOf(Locale.getDefault()) }
        var currency by remember { mutableStateOf(Currency.getInstance("JPY")) }

        Row {
            Button(onClick = { currency = Currency.getInstance("USD") }) {
                Text("US Dollar")
            }
            Button(onClick = { currency = Currency.getInstance("JPY") }) {
                Text("Yen")
            }
            Button(onClick = { currency = Currency.getInstance("EUR") }) {
                Text("Euro")
            }
        }
        Row {
            Button(onClick = { locale = Locale.getDefault() }) {
                Text("Default")
            }
            Button(onClick = { locale = Locale.JAPAN }) {
                Text("Japan")
            }
            Button(onClick = { locale = Locale.FRANCE }) {
                Text("France")
            }
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            readOnly = false,
            singleLine = true,
            maxLines = 1,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                textDecoration = TextDecoration.LineThrough
            ),
            colors = textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                leadingIconColor = MaterialTheme.colors.onSurface,
//                trailingIconColor = MaterialTheme.colors.onSurface,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
//            visualTransformation = me.khol.talaria.prefixSuffixTransformation(
//                prefix = numberFormat.positivePrefix,
//                suffix = numberFormat.positiveSuffix,
//            ),
            visualTransformation = currencyTransformation(locale, currency),
        )
    }
}

//1,234,567.89

fun currencyTransformation(
    locale: Locale,
    currency: Currency,
) = VisualTransformation { text: AnnotatedString ->
//    val input = -123456789.012345
    val input = text.text.toDoubleOrNull() ?: 0
    val numberFormat = (java.text.DecimalFormat.getCurrencyInstance(locale) as java.text.DecimalFormat).apply {
        this.currency = currency
    }
//    val symbols = DecimalFormat().decimalFormatSymbols
    val symbols = numberFormat.decimalFormatSymbols
    val thousandsSeparator = symbols.groupingSeparator
    val decimalSeparator = symbols.decimalSeparator

    val formattedText = numberFormat.format(input)

    TransformedText(
        text = AnnotatedString(formattedText),
        offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + numberFormat.positivePrefix.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return (offset - numberFormat.positivePrefix.length).coerceIn(0, text.length)
            }
        }
    )
}

fun prefixSuffixTransformation(
    prefix: String,
    suffix: String,
) = VisualTransformation { text: AnnotatedString ->
    TransformedText(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(textDecoration = TextDecoration.LineThrough)
            ) {
                append(prefix)
                append(text.text)
                append(suffix)
            }
        },
        offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                offset + prefix.length

            override fun transformedToOriginal(offset: Int): Int =
                (offset - prefix.length).coerceIn(0, text.length)
        }
    )
}

