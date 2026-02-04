package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = FirebaseFirestore.getInstance()

        // 1. Find Inputs
        val etImage = findViewById<EditText>(R.id.etCatImage)
        val etName = findViewById<EditText>(R.id.etCatName)
        val etBreed = findViewById<EditText>(R.id.etCatBreed)
        val etAge = findViewById<EditText>(R.id.etCatAge)
        val etDesc = findViewById<EditText>(R.id.etCatDesc)

        // 2. Find RadioGroups
        val rgEnergy = findViewById<RadioGroup>(R.id.rgAdminEnergy)
        val rgAffection = findViewById<RadioGroup>(R.id.rgAdminAffection)
        val rgSocial = findViewById<RadioGroup>(R.id.rgAdminSocial)
        val rgNoise = findViewById<RadioGroup>(R.id.rgAdminNoise)

        val btnSave = findViewById<Button>(R.id.btnSaveCat)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val breed = etBreed.text.toString().trim()
            val age = etAge.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val imageLink = etImage.text.toString().trim()

            // 3. Get Scores using the helper function
            val energyScore = getScore(rgEnergy)
            val affectionScore = getScore(rgAffection)
            val socialScore = getScore(rgSocial)
            val noiseScore = getScore(rgNoise)

            if (name.isEmpty() || imageLink.isEmpty()) {
                Toast.makeText(this, "Please fill in Name and Image Link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 4. Save to Firestore
            val catData = hashMapOf(
                "name" to name,
                "breed" to breed,
                "age" to age,
                "description" to desc,
                "imageUrl" to imageLink,
                "energyLevel" to energyScore,
                "affectionLevel" to affectionScore,
                "socialLevel" to socialScore,
                "noiseLevel" to noiseScore
            )

            db.collection("cats")
                .add(catData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Cat Added Successfully! 🐾", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Helper to get number from selected radio button
    // THIS IS THE PART THAT WAS MISSING! 👇
    private fun getScore(radioGroup: RadioGroup): Int {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) return 3 // Default to middle (3) if nothing selected
        val selectedButton = radioGroup.findViewById<RadioButton>(selectedId)
        // Extract the number from the text (e.g., "1" or "5")
        return selectedButton.text.toString().toIntOrNull() ?: 3
    }
}