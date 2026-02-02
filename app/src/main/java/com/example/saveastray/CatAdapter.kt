package com.example.saveastray

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter(private val catList: ArrayList<Cat>) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentCat = catList[position]
        holder.tvName.text = currentCat.name
        holder.tvBreed.text = currentCat.breed
        holder.tvAge.text = currentCat.age
        holder.itemView.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, DetailActivity::class.java)

            intent.putExtra("NAME", currentCat.name)
            intent.putExtra("BREED", currentCat.breed)
            intent.putExtra("AGE", currentCat.age)
            intent.putExtra("DESC", currentCat.description)
            intent.putExtra("IMAGE", currentCat.imageUrl)

            holder.itemView.context.startActivity(intent)
        }

        Glide.with(holder.itemView.context)
            .load(currentCat.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.ivCatImage)
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCatImage: ImageView = itemView.findViewById(R.id.ivCatImage)
        val tvName: TextView = itemView.findViewById(R.id.tvCatName)
        val tvBreed: TextView = itemView.findViewById(R.id.tvCatBreed)
        val tvAge: TextView = itemView.findViewById(R.id.tvCatAge)
    }
}