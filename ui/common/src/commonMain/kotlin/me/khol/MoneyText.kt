package me.khol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import me.khol.number.DecimalFormat
import me.khol.number.transformations.decimalNumberTransformation
import me.khol.number.transformations.fixedZeroTransformation
import me.khol.number.transformations.emptyTextTransformation

// TODO: Just use
//   * decimalNumberTransformation(fixedZerosPlaceholder, format),
@Composable
fun NumberText(
    value: String,
    format: DecimalFormat,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value, //decimalNumberTransformation(fixedZerosPlaceholder, format),
        style = textStyle,
        modifier = modifier,
    )
}

@Composable
fun DecimalNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    format: DecimalFormat,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    NumberTextField(
        value,
        onValueChange,
        format,
        textStyle,
        modifier,
        enabled,
        visualTransformation = {
            decimalNumberTransformation(
                annotatedString = it,
                format = format,
            )
        },
        placeholder = {
            Text(
                text = "0",
                style = textStyle,
            )
        }
    )
}

@Composable
fun FixedZeroNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    format: DecimalFormat,
    textStyle: TextStyle,
    fixedZeros: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    NumberTextField(
        value,
        onValueChange,
        format,
        textStyle,
        modifier,
        enabled,
        visualTransformation = {
            emptyTextTransformation(it) {
                fixedZeroTransformation(
                    annotatedString = it,
                    format = format,
                    fixedZeros = fixedZeros,
                )
            }
        },
        placeholder = {
            Text(
                text = fixedZeroTransformation(
                    string = "1",
                    format = format,
                    fixedZeros = fixedZeros,
                ),
                style = textStyle,
            )
        }
    )
}

/**
 * Having the currency symbol in a separate Text() leads to some issues:
 * * If the user clicks on the symbol, the TextField() will not gain focus
 * * If there is no input yet, the whole `placeholder` should be grayed out.
 *   Now we have to manually synchronize the state of TextField() with the
 *   symbol Text().
 * * The clickable area of the TextField has to be shrunk down in order
 *   to allow the currency symbol be directly attached to the number.
 *   * This creates yet another issue where the number is scrollable beyond
 *     the boundaries of the shrunk down TextField.
 * TODO: Do this only for the japanese text and keep other currencies grouped in a single TextField?
 */
@Composable
fun MoneyTextFieldScaffold(
    content: @Composable () -> Unit,
    error: @Composable () -> Unit = {},
    currencySymbol: String,
    currencyTextStyle: TextStyle,
//    currencyLocation: CurrencyLocation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
            Text(
                text = currencySymbol,
                style = currencyTextStyle,
            )
        }
        error()
    }
}
