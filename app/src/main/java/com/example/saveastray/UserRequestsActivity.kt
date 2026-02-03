package com.example.saveastray

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRequestsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: AdoptionAdapter
    private lateinit var requestList: ArrayList<AdoptionApplication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_requests)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        requestList = arrayListOf()

        adapter = AdoptionAdapter(requestList) {
        }

        val rv = findViewById<RecyclerView>(R.id.rvUserRequests)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<android.view.View>(R.id.btnBackUserApps).setOnClickListener { finish() }

        fetchMyRequests()
    }

    private fun fetchMyRequests() {
        val currentUser = auth.currentUser
        if (currentUser == null) return

        val myEmail = currentUser.email

        db.collection("adoptions")
            .whereEqualTo("userEmail", myEmail)
            .get()
            .addOnSuccessListener { documents ->
                requestList.clear()
                for (doc in documents) {
                    val request = doc.toObject(AdoptionApplication::class.java)
                    requestList.add(request)
                }
                adapter.notifyDataSetChanged()

                if (requestList.isEmpty()) {
                    Toast.makeText(this, "You haven't applied for any cats yet.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading status.", Toast.LENGTH_SHORT).show()
            }
    }
}