package me.khol

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.window.singleWindowApplication

//val notoSansCjkJpBoldFamily = FontFamily(Font(resource = "font/NotoSansCJKjp-Bold.otf"))

val notoSansJpFamily = FontFamily(
    Font(resource = "font/NotoSansJp-Black.otf", weight = FontWeight.Black, style = FontStyle.Normal),
    Font(resource = "font/NotoSansJp-Bold.otf", weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(resource = "font/NotoSansJp-Light.otf", weight = FontWeight.Light, style = FontStyle.Normal),
    Font(resource = "font/NotoSansJp-Medium.otf", weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(resource = "font/NotoSansJp-Regular.otf", weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(resource = "font/NotoSansJp-Thin.otf", weight = FontWeight.Thin, style = FontStyle.Normal),
)

val robotoFamily = FontFamily(
    Font(resource = "font/Roboto-Black.ttf", weight = FontWeight.Black, style = FontStyle.Normal),
    Font(resource = "font/Roboto-BlackItalic.ttf", weight = FontWeight.Black, style = FontStyle.Italic),
    Font(resource = "font/Roboto-Bold.ttf", weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(resource = "font/Roboto-BoldItalic.ttf", weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(resource = "font/Roboto-Italic.ttf", weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resource = "font/Roboto-Light.ttf", weight = FontWeight.Light, style = FontStyle.Normal),
    Font(resource = "font/Roboto-LightItalic.ttf", weight = FontWeight.Light, style = FontStyle.Italic),
    Font(resource = "font/Roboto-Medium.ttf", weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(resource = "font/Roboto-MediumItalic.ttf", weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(resource = "font/Roboto-Regular.ttf", weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(resource = "font/Roboto-Thin.ttf", weight = FontWeight.Thin, style = FontStyle.Normal),
    Font(resource = "font/Roboto-ThinItalic.ttf", weight = FontWeight.Thin, style = FontStyle.Italic),
)

fun main() = singleWindowApplication {
    FontMetrics(
        robotoFamily = robotoFamily,
        notoSansJpFamily = notoSansJpFamily,
        excludedFontPadding = PlatformTextStyle(null, null)
    )
}
