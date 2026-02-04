package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)

        val etPassword = findViewById<EditText>(R.id.etNewPassword)
        val btnUpdate = findViewById<Button>(R.id.btnUpdatePassword)
        val btnBack = findViewById<Button>(R.id.btnBackAdminSettings)

        btnUpdate.setOnClickListener {
            val newPass = etPassword.text.toString().trim()

            if (newPass.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                user.updatePassword(newPass)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show()
                        etPassword.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Error: No user logged in.", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener { finish() }
    }
}