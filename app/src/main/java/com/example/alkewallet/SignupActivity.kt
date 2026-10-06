package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnCreateAccount = findViewById<Button>(R.id.btn_create_account)
        val btnAlreadyAccount = findViewById<Button>(R.id.btn_already_account)

        // After signup, redirect to Login Page
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Redirect to Login Page
        btnAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
