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

        val etEmail = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_email)
        val etPassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_password)

        // Navigate to Home upon login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Por favor llena todos los campos", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = com.example.alkewallet.data.DatabaseHelper(this)
            if (dbHelper.loginUser(email, password)) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                android.widget.Toast.makeText(this, "Usuario o contraseña incorrectos", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Signup
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
