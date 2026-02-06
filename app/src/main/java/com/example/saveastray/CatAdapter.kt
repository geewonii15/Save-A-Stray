package com.example.saveastray

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class CatAdapter(
    private val context: Context,
    private val catList: MutableList<Cat>,
    private val isAdmin: Boolean
) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = catList[position]

        holder.tvName.text = cat.name
        holder.tvBreed.text = "${cat.breed} • ${cat.age}"
        holder.tvDesc.text = cat.description

        Glide.with(context)
            .load(cat.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImage)

        if (isAdmin) {
            holder.btnEdit.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.VISIBLE
        } else {
            holder.btnEdit.visibility = View.GONE
            holder.btnDelete.visibility = View.GONE
        }

        holder.btnDelete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            db.collection("cats").document(cat.id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Cat Deleted!", Toast.LENGTH_SHORT).show()
                    catList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, catList.size)
                }
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, AdminActivity::class.java)
            intent.putExtra("catId", cat.id)
            intent.putExtra("name", cat.name)
            intent.putExtra("breed", cat.breed)
            intent.putExtra("age", cat.age)
            intent.putExtra("description", cat.description)
            intent.putExtra("imageUrl", cat.imageUrl)

            intent.putExtra("energy", cat.energyLevel)
            intent.putExtra("affection", cat.affectionLevel)
            intent.putExtra("social", cat.socialLevel)
            intent.putExtra("noise", cat.noiseLevel)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = catList.size

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCatName)
        val tvBreed: TextView = itemView.findViewById(R.id.tvCatBreed)
        val tvDesc: TextView = itemView.findViewById(R.id.tvCatDesc)
        val ivImage: ImageView = itemView.findViewById(R.id.ivCatImage)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteCat)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditCat)
    }
}