package me.khol

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CurrencyPreview(
    val amount: String,
    val currency: String,
)

val currencyPreviews = listOf(
    CurrencyPreview("1,499", "円"),
    CurrencyPreview("1,499", "฿"),
    CurrencyPreview("1,499", "¥"),
    CurrencyPreview("1,499", "€"),
)

val LocalDebugEnabled = compositionLocalOf { false }
val LocalFontMetricsEnabled = compositionLocalOf { false }

fun Modifier.debugBorder(): Modifier = composed {
    if (LocalDebugEnabled.current) {
        this.border(1.dp, Color.Red)
    } else {
        this
    }
}

fun Modifier.drawFontData(fontData: FontData): Modifier = composed {
    if (LocalFontMetricsEnabled.current) {
        this.drawBehind {
            with(fontData) {
                drawLine(Color.Red, Offset(0f, ascent), Offset(size.width, ascent))
                drawLine(Color.Blue, Offset(0f, descent), Offset(size.width, descent))
                // The same as debugBorder()
//                drawLine(Color.Magenta, Offset(0f, top), Offset(size.width, top))
//                drawLine(Color.Magenta, Offset(0f, bottom), Offset(size.width, bottom))
                drawLine(Color.Black, Offset(0f, baseline), Offset(size.width, baseline))
            }
        }
    } else {
        this
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FontMetrics(
    robotoFamily: FontFamily,
    notoSansJpFamily: FontFamily,
    excludedFontPadding: PlatformTextStyle,
) {

    var isBorderEnabled by remember { mutableStateOf(false) }
    var isFontMetricsEnabled by remember { mutableStateOf(false) }
    var isIncludeFontPadding by remember { mutableStateOf(true) }

    CompositionLocalProvider(
        LocalDebugEnabled provides isBorderEnabled,
        LocalFontMetricsEnabled provides isFontMetricsEnabled,
    ) {

        var amountSize by remember { mutableStateOf(36f) }
        var currencySize by remember { mutableStateOf(30f) }

        val robotoStyle = TextStyle(
            fontSize = amountSize.sp,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Bold,
            platformStyle = if (isIncludeFontPadding) null else excludedFontPadding,
        )
        val notoSansStyle = robotoStyle.copy(
            fontFamily = notoSansJpFamily
        )

        @Composable
        fun MergedMoneyText(currency: CurrencyPreview, style: TextStyle) {
            var fontData by remember { mutableStateOf(FontData()) }
            Text(
                text = "${currency.amount}${currency.currency}",
                style = style,
                onTextLayout = { result ->
                    fontData = result.extractFontData(style)
                },
                modifier = Modifier
                    .debugBorder()
                    .drawFontData(fontData)
            )
        }

        @Composable
        fun SplitMoneyText(currency: CurrencyPreview, style: TextStyle, styleCurrency: TextStyle = style) {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var fontData1 by remember { mutableStateOf(FontData()) }
                    var fontData2 by remember { mutableStateOf(FontData()) }
                    Text(
                        text = currency.amount,
                        style = style,
                        onTextLayout = { result ->
                            fontData1 = result.extractFontData(style)
                        },
                        modifier = Modifier
//                            .alignByBaseline()
                            .debugBorder()
                            .drawFontData(fontData1)
                    )
                    Text(
                        text = currency.currency,
                        style = styleCurrency,
                        onTextLayout = { result ->
                            fontData2 = result.extractFontData(styleCurrency)
                        },
                        modifier = Modifier
//                            .alignByBaseline()
                            .debugBorder()
                            .drawFontData(fontData2)
                    )
                }
            }
        }

        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = isBorderEnabled,
                    onClick = {
                        isBorderEnabled = !isBorderEnabled
                    },
                ) {
                    Text("Border")
                }
                FilterChip(
                    selected = isFontMetricsEnabled,
                    onClick = {
                        isFontMetricsEnabled = !isFontMetricsEnabled
                    },
                ) {
                    Text("Font metrics")
                }
                FilterChip(
                    selected = isIncludeFontPadding,
                    onClick = {
                        isIncludeFontPadding = !isIncludeFontPadding
                    },
                ) {
                    Text("Include font padding")
                }
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
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Text("Roboto ${robotoStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        MergedMoneyText(it, robotoStyle)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    Text("Split Roboto ${robotoStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        SplitMoneyText(it, robotoStyle)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    Text("Noto ${notoSansStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        MergedMoneyText(it, notoSansStyle)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    Text("Split Noto ${notoSansStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        SplitMoneyText(it, notoSansStyle)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    Text("Roboto ${robotoStyle.fontSize.pretty} Noto ${notoSansStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        SplitMoneyText(it, robotoStyle, notoSansStyle)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    val currencyStyle = notoSansStyle.copy(fontSize = currencySize.sp)
                    Text("Roboto ${robotoStyle.fontSize.pretty} Noto ${currencyStyle.fontSize.pretty}")
                    currencyPreviews.forEach {
                        SplitMoneyText(it, robotoStyle, currencyStyle)
                    }
                }
            }
        }
    }
}


val TextUnit.pretty: String
    get() = "${"%,.0f".format(value)}"


data class FontData(
    val ascent: Float = 0f,
    val descent: Float = 0f,
    val top: Float = 0f,
    val bottom: Float = 0f,
    val baseline: Float = 0f,
)

fun TextLayoutResult.extractFontData(style: TextStyle): FontData {
    val fontSize = with(layoutInput.density) { style.fontSize.toPx() }
    val top = getLineTop(0)
    val bottom = getLineBottom(0)
    val baseline = firstBaseline
    val ascent = bottom - fontSize
    val descent = bottom - (baseline - fontSize - top)
    return FontData(
        top = top,
        bottom = bottom,
        baseline = baseline,
        ascent = ascent,
        descent = descent,
    )
}
