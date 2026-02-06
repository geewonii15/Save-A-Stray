package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var isEditMode = false
    private var catIdToEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = FirebaseFirestore.getInstance()

        val tvTitle = findViewById<TextView>(R.id.tvHeaderTitle) // This needs Step 4 to work!
        val etImage = findViewById<EditText>(R.id.etCatImage)
        val etName = findViewById<EditText>(R.id.etCatName)
        val etBreed = findViewById<EditText>(R.id.etCatBreed)
        val etAge = findViewById<EditText>(R.id.etCatAge)
        val etDesc = findViewById<EditText>(R.id.etCatDesc)

        val rgEnergy = findViewById<RadioGroup>(R.id.rgAdminEnergy)
        val rgAffection = findViewById<RadioGroup>(R.id.rgAdminAffection)
        val rgSocial = findViewById<RadioGroup>(R.id.rgAdminSocial)
        val rgNoise = findViewById<RadioGroup>(R.id.rgAdminNoise)

        val btnSave = findViewById<Button>(R.id.btnSaveCat)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        if (intent.hasExtra("catId")) {
            isEditMode = true
            catIdToEdit = intent.getStringExtra("catId")

            tvTitle.text = "Edit Cat ✏️"
            btnSave.text = "Update Cat"

            etName.setText(intent.getStringExtra("name"))
            etBreed.setText(intent.getStringExtra("breed"))
            etAge.setText(intent.getStringExtra("age"))
            etDesc.setText(intent.getStringExtra("description"))
            etImage.setText(intent.getStringExtra("imageUrl"))

            setRadioScore(rgEnergy, intent.getIntExtra("energy", 3))
            setRadioScore(rgAffection, intent.getIntExtra("affection", 3))
            setRadioScore(rgSocial, intent.getIntExtra("social", 3))
            setRadioScore(rgNoise, intent.getIntExtra("noise", 3))
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val breed = etBreed.text.toString().trim()
            val age = etAge.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val imageLink = etImage.text.toString().trim()

            if (name.isEmpty() || imageLink.isEmpty()) {
                Toast.makeText(this, "Please fill in Name and Image Link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val catData = hashMapOf(
                "name" to name,
                "breed" to breed,
                "age" to age,
                "description" to desc,
                "imageUrl" to imageLink,
                "energyLevel" to getScore(rgEnergy),
                "affectionLevel" to getScore(rgAffection),
                "socialLevel" to getScore(rgSocial),
                "noiseLevel" to getScore(rgNoise)
            )

            if (isEditMode && catIdToEdit != null) {
                db.collection("cats").document(catIdToEdit!!)
                    .set(catData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cat Updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                db.collection("cats")
                    .add(catData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cat Added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
        }
    }

    private fun getScore(radioGroup: RadioGroup): Int {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) return 3
        val selectedButton = radioGroup.findViewById<RadioButton>(selectedId)
        return selectedButton.text.toString().toIntOrNull() ?: 3
    }

    private fun setRadioScore(radioGroup: RadioGroup, score: Int) {
        for (i in 0 until radioGroup.childCount) {
            val button = radioGroup.getChildAt(i) as? RadioButton
            if (button?.text.toString() == score.toString()) {
                button?.isChecked = true
                break
            }
        }
    }
}