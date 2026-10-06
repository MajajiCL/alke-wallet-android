package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val repository = WalletRepository(applicationContext)
        viewModel = AuthViewModel(repository)

        val btnCreateAccount = findViewById<Button>(R.id.btn_create_account)
        val btnAlreadyAccount = findViewById<Button>(R.id.btn_already_account)

        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val etApellido = findViewById<EditText>(R.id.et_apellido)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<TextInputEditText>(R.id.et_password)
        val etRepassword = findViewById<TextInputEditText>(R.id.et_repassword)

        // Observar estado del ViewModel (MVVM)
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    btnCreateAccount.isEnabled = false
                }
                is Resource.Success -> {
                    btnCreateAccount.isEnabled = true
                    Toast.makeText(this, "¡Cuenta creada con éxito! Inicia sesión.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    btnCreateAccount.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCreateAccount.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val fullName = if (apellido.isNotBlank()) "$nombre $apellido" else nombre
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val repassword = etRepassword.text.toString().trim()

            viewModel.register(fullName, email, password, repassword)
        }

        btnAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
