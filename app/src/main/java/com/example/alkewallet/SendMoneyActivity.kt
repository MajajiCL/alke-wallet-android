package com.example.alkewallet

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.WalletViewModel

class SendMoneyActivity : AppCompatActivity() {

    private lateinit var viewModel: WalletViewModel
    private var currentUserEmail: String = "amanda@alkewallet.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_money)

        val repository = WalletRepository(applicationContext)
        viewModel = WalletViewModel(repository)

        val prefs = getSharedPreferences("alkewallet_session", Context.MODE_PRIVATE)
        currentUserEmail = prefs.getString("user_email", "amanda@alkewallet.com") ?: "amanda@alkewallet.com"

        val etAmount = findViewById<EditText>(R.id.et_amount)
        val etNote = findViewById<EditText>(R.id.et_note)
        val btnSend = findViewById<Button>(R.id.btn_send)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        // Observar resultado de la operación en el ViewModel
        viewModel.operationStatus.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    btnSend.isEnabled = false
                }
                is Resource.Success -> {
                    btnSend.isEnabled = true
                    Toast.makeText(this, state.data, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    btnSend.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        btnSend.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val noteStr = etNote.text.toString().trim()
            viewModel.sendMoney(currentUserEmail, "Yara Khalil", amountStr, noteStr)
        }
    }
}
