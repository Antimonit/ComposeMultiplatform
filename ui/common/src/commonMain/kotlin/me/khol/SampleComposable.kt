package me.khol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow

// state hoisting

@Composable
fun SampleComposable(
    isEnabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
) {
    TextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        textStyle = textStyle,
        enabled = isEnabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

var text = MutableStateFlow("")





