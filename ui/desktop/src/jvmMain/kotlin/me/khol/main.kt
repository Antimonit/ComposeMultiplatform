package me.khol

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class CurrencyPreview(
    val amount: String,
    val currency: String,
)

val currencyPreviews = listOf(
    CurrencyPreview("1,299", "円"),
    CurrencyPreview("1,299", "฿"),
    CurrencyPreview("1,299", "¥"),
    CurrencyPreview("1,299", "€"),
)

fun main() = singleWindowApplication {

//    MoneyText()
//
//    if (true)
//        return@singleWindowApplication

    var isBorderEnabled by remember { mutableStateOf(false) }
    var amountSize by remember { mutableStateOf(36f) }
    var currencySize by remember { mutableStateOf(30f) }

    val robotoStyle = TextStyle(
        fontSize = amountSize.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
    )
    val notoSansStyle = robotoStyle.copy(
        fontFamily = notoSansJpFamily
    )

    val borderModifier = if (isBorderEnabled)
        Modifier.border(1.dp, Color.Red)
    else
        Modifier

    @Composable
    fun MergedMoneyText(currency: CurrencyPreview, style: TextStyle) {
        Text(
            text = "${currency.amount}${currency.currency}",
            style = style,
            modifier = Modifier
                .then(borderModifier)
        )
    }

    @Composable
    fun SplitMoneyText(currency: CurrencyPreview, style: TextStyle, styleCurrency: TextStyle = style) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = currency.amount,
                style = style,
                modifier = Modifier
//                    .alignByBaseline()
                    .then(borderModifier)
            )
            Text(
                text = currency.currency,
                style = styleCurrency,
                modifier = Modifier
//                    .alignByBaseline()
                    .then(borderModifier)
            )
        }
    }

    Column {
        Row {
            Text("Border")
            Spacer(Modifier.size(4.dp))
            Switch(
                checked = isBorderEnabled,
                onCheckedChange = {
                    isBorderEnabled = it
                },
            )
        }
        Text("Amount size $amountSize")
        Slider(
            value = amountSize,
            onValueChange = {
                amountSize = it
            },
            valueRange = 6f..72f,
        )
        Text("Currency size $currencySize")
        Slider(
            value = currencySize,
            onValueChange = {
                currencySize = it
            },
            valueRange = 6f..72f,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column {
                Text("Roboto ${robotoStyle.fontSize}")
                currencyPreviews.forEach {
                    MergedMoneyText(it, robotoStyle)
                }
            }
            Column {
                Text("Split Roboto ${robotoStyle.fontSize}")
                currencyPreviews.forEach {
                    SplitMoneyText(it, robotoStyle)
                }
            }
            Column {
                Text("Noto ${notoSansStyle.fontSize}")
                currencyPreviews.forEach {
                    MergedMoneyText(it, notoSansStyle)
                }
            }
            Column {
                Text("Split Noto ${notoSansStyle.fontSize}")
                currencyPreviews.forEach {
                    SplitMoneyText(it, notoSansStyle)
                }
            }
            Column {
                Text("Roboto ${robotoStyle.fontSize} Noto ${notoSansStyle.fontSize}")
                currencyPreviews.forEach {
                    SplitMoneyText(it, robotoStyle, notoSansStyle)
                }
            }
            Column {
                Text("Roboto ${robotoStyle.fontSize} Noto $currencySize")
                currencyPreviews.forEach {
                    SplitMoneyText(it, robotoStyle, notoSansStyle.copy(fontSize = currencySize.sp))
                }
            }
        }
    }
}

