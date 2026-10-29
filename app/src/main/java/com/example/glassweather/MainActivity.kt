package com.example.glassweather

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("cfg", MODE_PRIVATE)

        val pad = (24 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(pad, pad, pad, pad)
        }
        root.addView(TextView(this).apply {
            text = "Укажите город для виджета (запасной источник), затем добавьте виджет «Glass Погода» на рабочий стол."
            textSize = 16f
        })
        val input = EditText(this).apply {
            hint = "Город"
            setText(prefs.getString("city", "Курчатов"))
            setSingleLine()
        }
        root.addView(input)
        root.addView(Button(this).apply {
            text = "Сохранить"
            setOnClickListener {
                prefs.edit().putString("city", input.text.toString().trim()).apply()
                WeatherWidget.refreshAll(this@MainActivity)
                Toast.makeText(this@MainActivity, "Сохранено", Toast.LENGTH_SHORT).show()
            }
        })

        val result = TextView(this).apply {
            textSize = 13f
            setTextIsSelectable(true)
        }
        root.addView(Button(this).apply {
            text = "Проверить Xiaomi"
            setOnClickListener { result.text = XiaomiWeather.dump(this@MainActivity) }
        })
        root.addView(ScrollView(this).apply { addView(result) })
        setContentView(root)
    }
}
