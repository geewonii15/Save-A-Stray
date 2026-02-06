package com.example.saveastray

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BrowseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var catList: ArrayList<Cat>
    private lateinit var catAdapter: CatAdapter
    private lateinit var db: FirebaseFirestore
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        isAdmin = intent.getBooleanExtra("IS_ADMIN", false)

        findViewById<ImageButton>(R.id.btnBackBrowse).setOnClickListener { finish() }

        recyclerView = findViewById(R.id.rvCatList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        catList = arrayListOf()

        catAdapter = CatAdapter(this, catList, isAdmin)
        recyclerView.adapter = catAdapter

        db = FirebaseFirestore.getInstance()
        loadCats()
    }

    private fun loadCats() {
        db.collection("cats").get()
            .addOnSuccessListener { documents ->
                catList.clear()
                for (document in documents) {
                    val cat = document.toObject(Cat::class.java)

                    cat.id = document.id

                    catList.add(cat)
                }
                catAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading cats", Toast.LENGTH_SHORT).show()
            }
    }
}