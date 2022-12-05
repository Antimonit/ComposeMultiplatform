package me.khol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.khol.number.DecimalFormat
import me.khol.number.decimalNumberTransformation
import me.khol.number.validateDecimalNumber
import java.math.BigDecimal
import java.util.*

@Composable
fun MoneyText() {
    var oldValue by remember { mutableStateOf(TextFieldValue()) }
    var value by remember { mutableStateOf(TextFieldValue()) }

    val number = value.text.toBigDecimalOrNull()
    val minValue = 1000
    val maxValue = 5000000

    val errorText = when {
        number == null -> "" // invalid but do not show error message
        number < BigDecimal(minValue) -> "The amount cannot be lower than $minValue"
        number > BigDecimal(maxValue) -> "The amount cannot be over $maxValue"
        else -> null
    }

    val format = DecimalFormat(Locale.US)
    Column {
        val textStyle = TextStyle(
            fontSize = 36.sp,
        )
        TextField(
            value = value,
            onValueChange = { newValue ->
                oldValue = newValue
                value = validateDecimalNumber(
                    format = format,
                    oldValue = value,
                    newValue = newValue,
                )
            },
            isError = errorText != null,
            textStyle = textStyle,
            placeholder = {
                Text(
                    text = "0",
                    style = textStyle,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = {
                // TODO: `minAmountUnit` 100
                // TODO: Display currency symbol
                decimalNumberTransformation(
                    annotatedString = it,
                    format = format,
                )
            },
        )
        if (errorText != null) {
            Text(
                text = errorText,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Old value:\n${oldValue}")
        Text(text = "New value:\n${value}")
    }
}
