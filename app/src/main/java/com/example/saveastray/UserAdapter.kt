package com.example.saveastray

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserAdapter(
    private val userList: MutableList<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_request, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvEmail.text = user.email

        holder.btnApprove.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            db.collection("users").document(user.id)
                .update("status", "approved")
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Admin Approved! ✅", Toast.LENGTH_SHORT).show()
                    userList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, userList.size)
                }
        }
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        val btnApprove: Button = itemView.findViewById(R.id.btnApproveAdmin)
    }
}