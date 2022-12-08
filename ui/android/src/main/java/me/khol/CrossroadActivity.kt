package me.khol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.khol.compose.playground.android.R

class CrossroadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.crossroad_activity)
        findViewById<Button>(R.id.money_text_button).setOnClickListener {
            startActivity(Intent(this, MoneyTextActivity::class.java))
        }
        findViewById<Button>(R.id.font_metrics_button).setOnClickListener {
            startActivity(Intent(this, FontMetricsActivity::class.java))
        }
    }
}
