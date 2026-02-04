package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Personalized Greeting
        val user = FirebaseAuth.getInstance().currentUser
        val tvName = findViewById<TextView>(R.id.tvUserName)
        if (user != null && user.email != null) {
            tvName.text = user.email
        }

        // 2. Navigation Buttons (Find Views safely)
        val btnBrowse = findViewById<View>(R.id.btnGoToBrowse)
        val btnQuiz = findViewById<View>(R.id.btnGoToQuiz)
        val btnProfile = findViewById<View>(R.id.btnGoToProfile) // New Button!
        val btnLogout = findViewById<Button>(R.id.btnUserLogout)

        btnBrowse.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
        }

        btnQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        btnProfile.setOnClickListener {
            // If you have a SettingsActivity, put it here.
            // If not, we will create one, or just show a message for now.
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}