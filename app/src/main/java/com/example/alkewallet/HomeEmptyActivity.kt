package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_empty)

        val btnSendMoney = findViewById<Button>(R.id.btn_send_money)
        val btnRequestMoney = findViewById<Button>(R.id.btn_request_money)
        val avatarImage = findViewById<ImageView>(R.id.avatarImage)

        btnSendMoney.setOnClickListener {
            startActivity(Intent(this, SendMoneyActivity::class.java))
        }

        btnRequestMoney.setOnClickListener {
            startActivity(Intent(this, RequestMoneyActivity::class.java))
        }

        avatarImage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
