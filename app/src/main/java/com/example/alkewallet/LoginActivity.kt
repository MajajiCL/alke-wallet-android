package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnCreateAccount = findViewById<Button>(R.id.btn_create_account)

        // Navigate to Home upon login
        btnLogin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Navigate to Signup
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
