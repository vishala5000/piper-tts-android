package com.pipertts.studio

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)

        text.text = """
            🎙️ Piper TTS Studio

            App started successfully.

            Android engine test
        """.trimIndent()

        text.textSize = 22f
        text.gravity = Gravity.CENTER
        text.setTextColor(Color.BLACK)
        text.setBackgroundColor(Color.WHITE)

        setContentView(text)
    }
}
