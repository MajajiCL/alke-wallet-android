package com.example.alkewallet

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RequestMoneyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_money)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_request).setOnClickListener {
            Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
