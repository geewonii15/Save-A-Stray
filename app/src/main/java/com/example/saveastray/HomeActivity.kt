package com.example.saveastray

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var catList: ArrayList<Cat>
    private lateinit var catAdapter: CatAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.rvCatList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        catList = arrayListOf()
        catAdapter = CatAdapter(catList)
        recyclerView.adapter = catAdapter

        db = FirebaseFirestore.getInstance()

        getCatData()

        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnMyApps = findViewById<TextView>(R.id.btnMyApps)
        btnMyApps.setOnClickListener {
            val intent = Intent(this, UserRequestsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCatData() {
        db.collection("cats")
            .get()
            .addOnSuccessListener { documents ->
                catList.clear()
                for (document in documents) {
                    val cat = document.toObject(Cat::class.java)
                    catList.add(cat)
                }
                catAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show()
            }
    }
}