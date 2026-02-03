package com.example.saveastray

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdoptionAdapter(
    private val requestList: ArrayList<AdoptionApplication>,
    private val onItemClick: (AdoptionApplication) -> Unit
) : RecyclerView.Adapter<AdoptionAdapter.AdoptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_adoption, parent, false)
        return AdoptionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        val currentRequest = requestList[position]

        holder.tvTitle.text = "Application for ${currentRequest.catName}"
        holder.tvEmail.text = "From: ${currentRequest.userEmail}"
        holder.tvStatus.text = "Status: ${currentRequest.status}"

        when (currentRequest.status) {
            "Approved" -> holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
            "Rejected" -> holder.tvStatus.setTextColor(Color.parseColor("#D32F2F"))
            else -> holder.tvStatus.setTextColor(Color.parseColor("#F57C00"))
        }

        holder.itemView.setOnClickListener {
            onItemClick(currentRequest)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class AdoptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvRequestTitle)
        val tvEmail: TextView = itemView.findViewById(R.id.tvRequestEmail)
        val tvStatus: TextView = itemView.findViewById(R.id.tvRequestStatus)
    }
}