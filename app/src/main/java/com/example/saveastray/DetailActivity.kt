package com.example.saveastray

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvBreed = findViewById<TextView>(R.id.tvDetailBreed)
        val tvAge = findViewById<TextView>(R.id.tvDetailAge)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDesc)
        val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
        val btnAdopt = findViewById<Button>(R.id.btnStartAdoption)

        val name = intent.getStringExtra("NAME")
        val breed = intent.getStringExtra("BREED")
        val age = intent.getStringExtra("AGE")
        val desc = intent.getStringExtra("DESC")
        val imageUrl = intent.getStringExtra("IMAGE")

        tvName.text = name
        tvBreed.text = breed
        tvAge.text = age
        tvDesc.text = desc

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(ivImage)

        btnAdopt.setOnClickListener {
            Toast.makeText(this, "Application submitted for $name!", Toast.LENGTH_LONG).show()
        }
    }
}