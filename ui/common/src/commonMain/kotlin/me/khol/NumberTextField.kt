package me.khol

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import me.khol.number.DecimalFormat
import me.khol.number.validateDecimalNumber
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    format: DecimalFormat,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation,
    placeholder: @Composable () -> Unit = {},
) {
    // TODO: This does not persist across configuration changes
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = value)) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = textFieldValue,
        onValueChange = { requestedValue ->
            textFieldValue = validateDecimalNumber(
                format = format,
                oldValue = textFieldValue,
                newValue = requestedValue,
            )
            if (value != textFieldValue.text) {
                onValueChange(textFieldValue.text)
            }
        },
        modifier = modifier.width(IntrinsicSize.Min), // TODO: Disable scrolling?
        enabled = enabled,
        maxLines = 1,
        singleLine = true,
        interactionSource = interactionSource,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                placeholder = placeholder,
                contentPadding = PaddingValues(all = 0.dp),
            )
        },
    )
}
