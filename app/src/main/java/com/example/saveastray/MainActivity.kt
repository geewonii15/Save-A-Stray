package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var tapCount = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.splash_logo)


        runnable = Runnable {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        handler.postDelayed(runnable, 3000)

        // 2. The "Secret Admin" Tap Listener
        logo.setOnClickListener {
            tapCount++

            if (tapCount == 5) {
                handler.removeCallbacks(runnable)

                Toast.makeText(this, "Admin Mode Unlocked!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("IS_ADMIN", true) // Send the secret signal
                startActivity(intent)
                finish()
            }
        }
    }
}