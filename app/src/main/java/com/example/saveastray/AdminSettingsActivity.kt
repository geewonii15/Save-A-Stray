package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminSettingsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userAdapter: UserAdapter
    private val pendingAdmins = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)

        db = FirebaseFirestore.getInstance()

        findViewById<Button>(R.id.btnBackAdminSettings).setOnClickListener {
            finish()
        }

        val etPassword = findViewById<EditText>(R.id.etNewPassword)
        val btnUpdate = findViewById<Button>(R.id.btnUpdatePassword)

        btnUpdate.setOnClickListener {
            val newPass = etPassword.text.toString().trim()
            if (newPass.length < 6) {
                Toast.makeText(this, "Password must be at least 6 chars", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = FirebaseAuth.getInstance().currentUser
            user?.updatePassword(newPass)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password Updated!", Toast.LENGTH_SHORT).show()
                    etPassword.text.clear()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val rvPending = findViewById<RecyclerView>(R.id.rvPendingAdmins)
        rvPending.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(pendingAdmins)
        rvPending.adapter = userAdapter

        loadPendingAdmins()
    }

    private fun loadPendingAdmins() {
        db.collection("users")
            .whereEqualTo("role", "admin")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { documents ->
                pendingAdmins.clear()
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    user.id = document.id
                    pendingAdmins.add(user)
                }
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading requests", Toast.LENGTH_SHORT).show()
            }
    }
}