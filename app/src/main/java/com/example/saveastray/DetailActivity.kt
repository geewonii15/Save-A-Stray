package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvBreed = findViewById<TextView>(R.id.tvDetailBreed)
        val tvAge = findViewById<TextView>(R.id.tvDetailAge)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDesc)
        val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
        val btnAdopt = findViewById<Button>(R.id.btnStartAdoption)

        val catName = intent.getStringExtra("NAME") ?: "Unknown"
        val breed = intent.getStringExtra("BREED")
        val age = intent.getStringExtra("AGE")
        val desc = intent.getStringExtra("DESC")
        val imageUrl = intent.getStringExtra("IMAGE")

        tvName.text = catName
        tvBreed.text = breed
        tvAge.text = age
        tvDesc.text = desc

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(ivImage)

        btnAdopt.setOnClickListener {
            submitAdoptionRequest(catName)
        }
    }

    private fun submitAdoptionRequest(catName: String) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        val email = currentUser.email ?: "No Email"

        val applicationData = hashMapOf(
            "userEmail" to email,
            "catName" to catName,
            "status" to "Pending",
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("adoptions")
            .add(applicationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Application Sent!", Toast.LENGTH_LONG).show()
                findViewById<Button>(R.id.btnStartAdoption).isEnabled = false
                findViewById<Button>(R.id.btnStartAdoption).text = "Request Sent"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}