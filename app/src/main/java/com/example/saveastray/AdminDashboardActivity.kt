package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        db = FirebaseFirestore.getInstance()

        findViewById<ConstraintLayout>(R.id.btnManageCatsCard).setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.btnRequestsCard).setOnClickListener {
            startActivity(Intent(this, AdminRequestsActivity::class.java))
        }

        findViewById<ConstraintLayout>(R.id.btnViewCatsCard).setOnClickListener {
            val intent = Intent(this, BrowseActivity::class.java)
            intent.putExtra("IS_ADMIN", true)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAdminLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.btnAdminSettingsCard).setOnClickListener {
            startActivity(Intent(this, AdminSettingsActivity::class.java))
        }

        loadDashboardStats()
    }

    private fun loadDashboardStats() {
        val tvTotalCats = findViewById<TextView>(R.id.tvTotalCats)
        val tvPendingRequests = findViewById<TextView>(R.id.tvPendingRequests)

        db.collection("cats").get().addOnSuccessListener { documents ->
            tvTotalCats.text = "Total Active: ${documents.size()}"
        }

        db.collection("adoptions").whereEqualTo("status", "Pending").get()
            .addOnSuccessListener { documents ->
                tvPendingRequests.text = "Pending Review: ${documents.size()}"
            }
    }
}