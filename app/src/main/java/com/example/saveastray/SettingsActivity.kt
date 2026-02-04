package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnRequests = findViewById<Button>(R.id.btnViewMyRequests)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // 1. View My Requests
        btnRequests.setOnClickListener {
            // Assuming you have this activity. If not, we can create it next!
            val intent = Intent(this, UserRequestsActivity::class.java)
            startActivity(intent)
        }

        // 2. Back Button
        btnBack.setOnClickListener {
            finish() // Simply closes this page and goes back to Home
        }
    }
}