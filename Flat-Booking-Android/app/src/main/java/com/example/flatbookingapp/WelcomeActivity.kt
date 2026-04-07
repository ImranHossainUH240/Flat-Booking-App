package com.example.flatbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
            finish() // This prevents the user from going back to the welcome screen
        }
    }
}