package com.example.alkewallet

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        val ivAvatar = findViewById<ImageView>(R.id.iv_avatar)
        
        // Integración con Picasso para carga asíncrona de imagen de perfil
        try {
            Picasso.get()
                .load("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400")
                .placeholder(R.drawable.profile_image)
                .error(R.drawable.profile_image)
                .into(ivAvatar)
        } catch (e: Exception) {
            ivAvatar.setImageResource(R.drawable.profile_image)
        }
    }
}
