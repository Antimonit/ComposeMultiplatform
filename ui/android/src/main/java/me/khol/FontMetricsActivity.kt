package me.khol

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily

class FontMetricsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FontMetrics(
                robotoFamily = FontFamily.Default,
                notoSansJpFamily = FontFamily.Default,
                excludedFontPadding = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        }
    }
}
