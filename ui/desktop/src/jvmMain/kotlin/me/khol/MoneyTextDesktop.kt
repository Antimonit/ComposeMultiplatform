package me.khol

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
    NumberTextField(
        PlatformTextStyle(null, null)
    )
}
