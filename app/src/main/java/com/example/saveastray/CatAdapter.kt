package com.example.saveastray

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
    private val catList: ArrayList<Cat>,
    private val isAdmin: Boolean
) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCatName)
        val tvBreed: TextView = itemView.findViewById(R.id.tvCatBreed)
        val tvDesc: TextView = itemView.findViewById(R.id.tvCatDesc)
        val ivImage: ImageView = itemView.findViewById(R.id.ivCatImage)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteCat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = catList[position]

        holder.tvName.text = cat.name
        holder.tvBreed.text = "${cat.breed} • ${cat.age}"
        holder.tvDesc.text = cat.description

        Glide.with(holder.itemView.context)
            .load(cat.imageUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImage)

        holder.itemView.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("CAT_NAME", cat.name)
            intent.putExtra("CAT_BREED", cat.breed)
            intent.putExtra("CAT_AGE", cat.age)
            intent.putExtra("CAT_DESC", cat.description)
            intent.putExtra("CAT_IMAGE", cat.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        if (isAdmin) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener {
                deleteCat(cat, position, holder.itemView)
            }
        } else {
            holder.btnDelete.visibility = View.GONE
        }
    }

    private fun deleteCat(cat: Cat, position: Int, view: View) {
        val db = FirebaseFirestore.getInstance()

        db.collection("cats")
            .whereEqualTo("name", cat.name)
            .whereEqualTo("description", cat.description)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("cats").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(view.context, "Cat Deleted!", Toast.LENGTH_SHORT).show()
                            catList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                }
            }
    }

    override fun getItemCount(): Int {
        return catList.size
    }
}