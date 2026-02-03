package com.example.saveastray

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminRequestsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: AdoptionAdapter
    private lateinit var requestList: ArrayList<AdoptionApplication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_requests)

        db = FirebaseFirestore.getInstance()
        requestList = arrayListOf()

        adapter = AdoptionAdapter(requestList) { selectedRequest ->
            showReviewDialog(selectedRequest)
        }

        val rvRequests = findViewById<RecyclerView>(R.id.rvRequests)
        rvRequests.layoutManager = LinearLayoutManager(this)
        rvRequests.adapter = adapter

        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBackRequests)
        btnBack.setOnClickListener { finish() }

        fetchRequests()
    }

    private fun fetchRequests() {
        db.collection("adoptions")
            .get()
            .addOnSuccessListener { documents ->
                requestList.clear()
                for (doc in documents) {
                    val request = doc.toObject(AdoptionApplication::class.java)
                    request.applicationId = doc.id
                    requestList.add(request)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun showReviewDialog(request: AdoptionApplication) {
        val options = arrayOf("Approve", "Reject")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Review Application for ${request.catName}")
        builder.setItems(options) { _, which ->
            if (which == 0) {
                updateStatus(request, "Approved")
            } else {
                updateStatus(request, "Rejected")
            }
        }
        builder.show()
    }

    private fun updateStatus(request: AdoptionApplication, newStatus: String) {
        db.collection("adoptions").document(request.applicationId)
            .update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(this, "Marked as $newStatus", Toast.LENGTH_SHORT).show()
                request.status = newStatus
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
    }
}