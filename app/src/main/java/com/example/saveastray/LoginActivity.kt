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

        val isAdmin = intent.getBooleanExtra("IS_ADMIN", false)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        if (isAdmin) {
            loginButton.text = "Login as Admin"
            loginButton.backgroundTintList = getColorStateList(R.color.sage_green)
        }

        loginButton.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        if (userId != null) {
                            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener { document ->
                                    val role = document.getString("role") ?: "user"
                                    val status = document.getString("status") ?: "pending"

                                    if (isAdmin) {
                                        if (role == "admin" && status == "approved") {
                                            Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                                            finish()
                                        } else if (role == "admin" && status != "approved") {
                                            Toast.makeText(this, "Account Pending Approval.", Toast.LENGTH_LONG).show()
                                            auth.signOut()
                                        } else {
                                            Toast.makeText(this, "Access Denied: You are not an Admin.", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                        }
                                    }
                                    else {
                                        startActivity(Intent(this, HomeActivity::class.java))
                                        finish()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error verifying account.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        val registerLink = findViewById<TextView>(R.id.tvRegisterLink)
        registerLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}