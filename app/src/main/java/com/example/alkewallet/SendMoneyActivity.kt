package com.example.alkewallet

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SendMoneyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_money)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_send).setOnClickListener {
            Toast.makeText(this, "Dinero enviado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
