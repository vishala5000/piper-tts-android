package com.pipertts.studio

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : Activity() {

    companion object {

        private const val MODEL_URL =
            "https://github.com/vishala5000/piper-tts-android/releases/download/v1.0-model/en_US-ryan-high.onnx"

        private const val CONFIG_URL =
            "https://github.com/vishala5000/piper-tts-android/releases/download/v1.0-model/en_US-ryan-high.onnx.json"

        private const val MODEL_FILE =
            "en_US-ryan-high.onnx"

        private const val CONFIG_FILE =
            "en_US-ryan-high.onnx.json"
    }

    private lateinit var status: TextView
    private lateinit var progress: ProgressBar
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUI()

        if (modelReady()) {
            showReady()
        } else {
            button.setOnClickListener {
                downloadModels()
            }
        }
    }

    private fun createUI() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)
        }

        val title = TextView(this).apply {
            text = "🎙️ Piper TTS Studio"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(17, 24, 39))
        }

        status = TextView(this).apply {
            text = "Checking voice model..."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 30)
        }

        progress = ProgressBar(
            this,
            null,
            android.R.attr.progressBarStyleHorizontal
        ).apply {
            max = 100
            progress = 0
        }

        button = Button(this).apply {
            text = "Download Voice Model"
        }

        root.addView(title)

        root.addView(
            status,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            progress,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                60
            )
        )

        root.addView(button)

        setContentView(root)
    }

    private fun modelReady(): Boolean {

        val model = File(filesDir, MODEL_FILE)
        val config = File(filesDir, CONFIG_FILE)

        return model.exists() &&
                config.exists() &&
                model.length() > 1_000_000 &&
                config.length() > 100
    }

    private fun showReady() {

        status.text =
            "✓ Voice model ready\n\n" +
            "en_US-ryan-high\n\n" +
            "Ready for offline TTS."

        progress.progress = 100

        button.text = "Voice Model Ready"
        button.isEnabled = false
    }

    private fun downloadModels() {

        button.isEnabled = false

        status.text =
            "Downloading voice model...\n\n" +
            "Please keep the app open."

        thread {

            try {

                downloadFile(
                    MODEL_URL,
                    File(filesDir, MODEL_FILE)
                )

                runOnUiThread {
                    status.text =
                        "Model downloaded.\n" +
                        "Downloading configuration..."
                }

                downloadFile(
                    CONFIG_URL,
                    File(filesDir, CONFIG_FILE)
                )

                runOnUiThread {
                    showReady()

                    Toast.makeText(
                        this,
                        "Voice model installed successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                runOnUiThread {

                    status.text =
                        "❌ Download failed\n\n" +
                        (e.message ?: "Unknown error")

                    button.text = "Try Again"
                    button.isEnabled = true
                }
            }
        }
    }

    private fun downloadFile(
        urlString: String,
        destination: File
    ) {

        val url = URL(urlString)

        val connection =
            url.openConnection() as HttpURLConnection

        connection.connectTimeout = 30000
        connection.readTimeout = 60000
        connection.instanceFollowRedirects = true

        connection.connect()

        if (connection.responseCode !in 200..299) {

            throw Exception(
                "HTTP ${connection.responseCode}"
            )
        }

        val total = connection.contentLengthLong

        connection.inputStream.use { input ->

            FileOutputStream(destination).use { output ->

                val buffer = ByteArray(8192)

                var downloaded = 0L

                while (true) {

                    val count =
                        input.read(buffer)

                    if (count == -1)
                        break

                    output.write(
                        buffer,
                        0,
                        count
                    )

                    downloaded += count

                    if (total > 0) {

                        val percent =
                            ((downloaded * 100) / total)
                                .toInt()

                        runOnUiThread {
                            progress.progress = percent

                            status.text =
                                "Downloading voice model...\n\n" +
                                "$percent%"
                        }
                    }
                }
            }
        }

        connection.disconnect()
    }
}
