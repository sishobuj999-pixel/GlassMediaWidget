package com.sed.glassmediawidget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val root = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(48, 64, 48, 48)
            }
            root.addView(TextView(this).apply {
                text = "Glass Media Widget"
                textSize = 24f
            })
            root.addView(TextView(this).apply {
                text = "App started successfully. Add the widget to your home screen, then enable notification access."
                textSize = 16f
                setPadding(0, 24, 0, 24)
            })
            root.addView(Button(this).apply {
                text = "Enable notification access"
                setOnClickListener {
                    startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
            })
            setContentView(ScrollView(this).apply { addView(root) })
        } catch (error: Throwable) {
            showStartupError(error)
        }
    }

    private fun showStartupError(error: Throwable) {
        val details = error.stackTraceToString()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
        }
        root.addView(TextView(this).apply {
            text = "Glass Media Widget startup error"
            textSize = 20f
        })
        root.addView(TextView(this).apply {
            text = details
            textSize = 11f
            setTextIsSelectable(true)
        })
        setContentView(ScrollView(this).apply { addView(root) })
    }
}
