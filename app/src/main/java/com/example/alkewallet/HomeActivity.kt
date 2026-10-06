package com.example.alkewallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.WalletViewModel
import com.squareup.picasso.Picasso
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: WalletViewModel
    private var currentUserEmail: String = "amanda@alkewallet.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicialización de Arquitectura MVVM
        val repository = WalletRepository(applicationContext)
        viewModel = WalletViewModel(repository)

        val prefs = getSharedPreferences("alkewallet_session", Context.MODE_PRIVATE)
        currentUserEmail = prefs.getString("user_email", "amanda@alkewallet.com") ?: "amanda@alkewallet.com"
        val currentUserName = prefs.getString("user_name", "Amanda") ?: "Amanda"

        val tvGreeting = findViewById<TextView>(R.id.tv_greeting)
        val tvBalanceAmount = findViewById<TextView>(R.id.tv_balance_amount)
        val btnSendMoney = findViewById<Button>(R.id.btn_send_money)
        val btnRequestMoney = findViewById<Button>(R.id.btn_request_money)
        val avatarImage = findViewById<ImageView>(R.id.avatarImage)

        tvGreeting.text = "Hola, $currentUserName!"

        // Carga de avatar asíncrona con Picasso
        try {
            Picasso.get()
                .load("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400")
                .placeholder(R.drawable.profile_image)
                .error(R.drawable.profile_image)
                .into(avatarImage)
        } catch (e: Exception) {
            avatarImage.setImageResource(R.drawable.profile_image)
        }

        // Observar balance reactivo desde el ViewModel (LiveData)
        viewModel.balance.observe(this) { newBalance ->
            tvBalanceAmount.text = String.format(Locale.US, "$%.2f", newBalance)
        }

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

    override fun onResume() {
        super.onResume()
        // Recargar datos y sincronizar al volver de otra pantalla
        viewModel.loadWalletData(currentUserEmail)
    }
}
