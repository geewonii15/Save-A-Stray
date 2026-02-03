package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        db = FirebaseFirestore.getInstance()

        val etImage = findViewById<EditText>(R.id.etCatImage)
        val etName = findViewById<EditText>(R.id.etCatName)
        val etBreed = findViewById<EditText>(R.id.etCatBreed)
        val etAge = findViewById<EditText>(R.id.etCatAge)
        val etDesc = findViewById<EditText>(R.id.etCatDesc)
        val btnSave = findViewById<Button>(R.id.btnSaveCat)

        btnSave.setOnClickListener {
            val imageLink = etImage.text.toString().trim()
            val name = etName.text.toString().trim()
            val breed = etBreed.text.toString().trim()
            val age = etAge.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (name.isEmpty() || imageLink.isEmpty()) {
                Toast.makeText(this, "Please fill in Name and Image Link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val catData = hashMapOf(
                "name" to name,
                "breed" to breed,
                "age" to age,
                "description" to desc,
                "imageUrl" to imageLink
            )

            db.collection("cats")
                .add(catData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Success! Cat Added.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}