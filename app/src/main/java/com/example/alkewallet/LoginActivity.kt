package com.example.alkewallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val repository = WalletRepository(applicationContext)
        viewModel = AuthViewModel(repository)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnCreateAccount = findViewById<Button>(R.id.btn_create_account)
        val etEmail = findViewById<TextInputEditText>(R.id.et_email)
        val etPassword = findViewById<TextInputEditText>(R.id.et_password)

        // Observar estado del ViewModel (Patrón MVVM)
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    btnLogin.isEnabled = true
                    val user = state.data
                    // Gestión de sesión segura (SharedPreferences)
                    val prefs = getSharedPreferences("alkewallet_session", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putString("user_email", user.email)
                        .putString("user_name", user.name)
                        .putString("auth_token", user.token ?: "demo_token")
                        .apply()

                    Toast.makeText(this, "¡Bienvenido, ${user.name}!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
