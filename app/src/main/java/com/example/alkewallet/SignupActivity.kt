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

        val etNombre = findViewById<android.widget.EditText>(R.id.et_nombre)
        val etApellido = findViewById<android.widget.EditText>(R.id.et_apellido)
        val etEmail = findViewById<android.widget.EditText>(R.id.et_email)
        val etPassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_password)
        val etRepassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_repassword)

        // After signup, redirect to Login Page
        btnCreateAccount.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val repassword = etRepassword.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Por favor llena todos los campos", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repassword) {
                android.widget.Toast.makeText(this, "Las contraseñas no coinciden", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = com.example.alkewallet.data.DatabaseHelper(this)
            
            if (dbHelper.checkUser(email)) {
                android.widget.Toast.makeText(this, "El correo ya está registrado", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertUser("$nombre $apellido", email, password)
                if (success) {
                    android.widget.Toast.makeText(this, "Cuenta creada con éxito!", android.widget.Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    android.widget.Toast.makeText(this, "Error al crear la cuenta", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Redirect to Login Page
        btnAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
