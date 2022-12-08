package me.khol

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.compose.foundation.layout.width
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import me.khol.compose.playground.android.R
import java.math.BigDecimal
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import me.khol.number.DecimalFormat
import java.util.*

class MoneyTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.money_text_activity)
        val moneyText = findViewById<MoneyText>(R.id.money_text)
        moneyText.isEnabled = false

        lifecycleScope.launchWhenCreated {
            delay(2000)
            moneyText.isEnabled = true
            delay(2000)
            moneyText.minValue = 10000
            delay(2000)
            moneyText.amountFontSize = 70.sp
            delay(2000)
            moneyText.amountFontSize = 36.sp
        }

        println("Create")

        lifecycleScope.launchWhenCreated {
            moneyText.state.collect {
                println(it)
            }
        }
    }
}

val notoSansJpFamily = FontFamily(
    Font(resId = R.font.noto_sans_jp_black, weight = FontWeight.Black, style = FontStyle.Normal),
    Font(resId = R.font.noto_sans_jp_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(resId = R.font.noto_sans_jp_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(resId = R.font.noto_sans_jp_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(resId = R.font.noto_sans_jp_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(resId = R.font.noto_sans_jp_thin, weight = FontWeight.Thin, style = FontStyle.Normal),
)

class MoneyText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var _enabled: Boolean by mutableStateOf(false)
    var value by mutableStateOf("")

    val state = snapshotFlow { value }

    var amountFontSize by mutableStateOf(36.sp)
    var currencyFontSize by mutableStateOf(30.sp)
    var minValue by mutableStateOf(1000)
    var maxValue by mutableStateOf(5_000_000)

    override fun isEnabled(): Boolean {
        return _enabled
    }

    override fun setEnabled(enabled: Boolean) {
        _enabled = enabled
    }

    @Composable
    override fun Content() {
        Column {
            val fixedZeros = 3
            val minUnit = BigDecimal.TEN.pow(fixedZeros)
            val numberValue = value.toBigDecimalOrNull()?.times(minUnit)

            val state = when {
                numberValue == null -> State.Invalid.NotANumber
                numberValue < BigDecimal(minValue) -> State.Invalid.UnderMinAmount
                numberValue > BigDecimal(maxValue) -> State.Invalid.OverMaxAmount
                else -> State.Valid(numberValue)
            }

            val textStyle = TextStyle(
                fontSize = amountFontSize,
//                fontFamily = FontFamily.Default,
//                fontWeight = FontWeight.SemiBold,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            )
            val currencyTextStyle = textStyle.copy(
                fontSize = currencyFontSize,
            )
            val decimalFormat = DecimalFormat(
                locale = Locale.US,
                maximumFractionDigits = 2,
                allowNegative = false,
            )

            // TODO: This
            val style1 = LocalTextStyle.current.merge(textStyle)
            // TODO: Or
            val style2 = MaterialTheme.typography.h6

            MoneyTextFieldScaffold(
                currencySymbol = "円",
                currencyTextStyle = currencyTextStyle,
                content = {
                    FixedZeroNumberTextField(
                        value = value,
                        onValueChange = { value = it },
                        format = decimalFormat,
                        fixedZeros = fixedZeros,
                        textStyle = textStyle,
                        modifier = Modifier.width(IntrinsicSize.Min)
                    )
                },
                error = {
                    when (state) {
                        is State.Valid -> Unit
                        is State.Invalid -> Text(
                            text = when (state) {
                                is State.Invalid.UnderMinAmount -> "The amount cannot be lower than $minValue"
                                is State.Invalid.OverMaxAmount -> "The amount cannot be over $maxValue"
                                is State.Invalid.NotANumber -> ""
                            },
                        )
                    }
                },
            )
        }
    }

    sealed interface State {
        class Valid(val number: BigDecimal) : State

        sealed interface Invalid : State {
            object NotANumber : Invalid
            object OverMaxAmount : Invalid
            object UnderMinAmount : Invalid
        }
    }
}

fun <T> stateSaver(delegate: Saver<T, Any>) =
    Saver<MutableState<T>, Any>(
        save = { state ->
            with(delegate) {
                save(state.value)
            }
        },
        restore = { value ->
            with(delegate) {
                @Suppress("UNCHECKED_CAST")
                mutableStateOf(restore(value) as T)
            }
        }
    )