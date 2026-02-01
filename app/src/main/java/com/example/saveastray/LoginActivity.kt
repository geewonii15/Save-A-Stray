package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // 1. Get the "Mode" (Admin vs. Regular User)
        val isAdmin = intent.getBooleanExtra("IS_ADMIN", false)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        // 2. Adjust UI for Admin
        if (isAdmin) {
            loginButton.text = "Login as Admin"
            loginButton.backgroundTintList = getColorStateList(R.color.sage_green) // Optional visual cue
        }

        // 3. Handle Login Click
        loginButton.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 4. Talk to Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()

                        // DECISION POINT: Where do we go?
                        if (isAdmin) {
                            // TODO: Go to Admin Dashboard (We will build this next!)
                            Toast.makeText(this, "Redirecting to Admin Dashboard...", Toast.LENGTH_SHORT).show()
                        } else {
                            // TODO: Go to User Home Screen (Adoption Feed)
                            Toast.makeText(this, "Redirecting to Home Feed...", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // 5. Navigation to Sign Up
        val registerLink = findViewById<TextView>(R.id.tvRegisterLink)
        registerLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}