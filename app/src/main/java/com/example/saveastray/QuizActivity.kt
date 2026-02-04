package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

class QuizActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        db = FirebaseFirestore.getInstance()

        // 1. Find the RadioGroups (The containers for our buttons)
        val rgEnergy = findViewById<RadioGroup>(R.id.rgEnergy)
        val rgAffection = findViewById<RadioGroup>(R.id.rgAffection)
        val rgSocial = findViewById<RadioGroup>(R.id.rgSocial)
        val rgNoise = findViewById<RadioGroup>(R.id.rgNoise)
        val btnFind = findViewById<Button>(R.id.btnFindMatch)

        btnFind.setOnClickListener {
            // 2. Get the score (1-5) from each group
            val energyScore = getScore(rgEnergy)
            val affectionScore = getScore(rgAffection)
            val socialScore = getScore(rgSocial)
            val noiseScore = getScore(rgNoise)

            if (energyScore == 0 || affectionScore == 0 || socialScore == 0 || noiseScore == 0) {
                Toast.makeText(this, "Please answer all questions! 🐾", Toast.LENGTH_SHORT).show()
            } else {
                findBestMatch(energyScore, affectionScore, socialScore, noiseScore)
            }
        }
    }

    private fun getScore(radioGroup: RadioGroup): Int {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) return 0 // Nothing selected

        val selectedButton = radioGroup.findViewById<RadioButton>(selectedId)
        return selectedButton.text.toString().toIntOrNull() ?: 3
    }

    private fun findBestMatch(uEnergy: Int, uAffection: Int, uSocial: Int, uNoise: Int) {
        db.collection("cats").get()
            .addOnSuccessListener { documents ->
                var bestCat: Cat? = null
                var lowestDiff = 999

                for (doc in documents) {
                    val cat = doc.toObject(Cat::class.java)

                    val diffEnergy = abs(cat.energyLevel - uEnergy)
                    val diffAffection = abs(cat.affectionLevel - uAffection)
                    val diffSocial = abs(cat.socialLevel - uSocial)
                    val diffNoise = abs(cat.noiseLevel - uNoise)

                    val totalDiff = diffEnergy + diffAffection + diffSocial + diffNoise

                    if (totalDiff < lowestDiff) {
                        lowestDiff = totalDiff
                        bestCat = cat
                    }
                }

                if (bestCat != null) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("CAT_NAME", bestCat.name)
                    intent.putExtra("CAT_BREED", bestCat.breed)
                    intent.putExtra("CAT_AGE", bestCat.age)
                    intent.putExtra("CAT_DESC", bestCat.description)
                    intent.putExtra("CAT_IMAGE", bestCat.imageUrl)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "No cats found!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}