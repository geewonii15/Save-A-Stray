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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = FirebaseFirestore.getInstance()

        val name = intent.getStringExtra("CAT_NAME") ?: ""
        val breed = intent.getStringExtra("CAT_BREED") ?: ""
        val age = intent.getStringExtra("CAT_AGE") ?: ""
        val desc = intent.getStringExtra("CAT_DESC") ?: ""
        val imageUrl = intent.getStringExtra("CAT_IMAGE") ?: ""

        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvDetailBreed).text = breed
        findViewById<TextView>(R.id.tvDetailAge).text = age
        findViewById<TextView>(R.id.tvDetailDesc).text = desc

        val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
        Glide.with(this).load(imageUrl).into(ivImage)

        findViewById<Button>(R.id.btnBackDetail).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnAdoptMe).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(this, "Please login first!", Toast.LENGTH_SHORT).show()
            } else {
                submitAdoptionRequest(user.email ?: "Unknown", name)
            }
        }
    }

    private fun submitAdoptionRequest(email: String, catName: String) {
        val request = hashMapOf(
            "adopterEmail" to email,
            "catName" to catName,
            "status" to "Pending",
            "date" to SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )

        db.collection("adoptions")
            .add(request)
            .addOnSuccessListener {
                Toast.makeText(this, "Request Sent! Check your Inbox.", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send request.", Toast.LENGTH_SHORT).show()
            }
    }
}