package com.example.alkewallet.data.repository

import android.content.Context
import com.example.alkewallet.data.DatabaseHelper
import com.example.alkewallet.data.model.LoginRequest
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.model.TransactionItem
import com.example.alkewallet.data.model.TransactionRequest
import com.example.alkewallet.data.model.UserProfile
import com.example.alkewallet.data.remote.RetrofitClient
import com.example.alkewallet.data.remote.WalletApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WalletRepository(
    private val context: Context,
    private val apiService: WalletApiService = RetrofitClient.apiService,
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
) {

    // --- Autenticación (Retrofit + Fallback Local) ---
    suspend fun login(email: String, pass: String): Resource<UserProfile> = withContext(Dispatchers.IO) {
        try {
            // 1. Intentar con la API REST externa (Retrofit)
            val response = apiService.login(LoginRequest(email, pass))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val user = UserProfile(
                    id = body.userId,
                    name = body.name,
                    email = body.email,
                    balance = 124.57,
                    token = body.token
                )
                return@withContext Resource.Success(user)
            }
        } catch (e: Exception) {
            // Error de red (modo offline) -> Proceder con la base de datos local
        }

        // 2. Fallback a la Base de Datos Local
        val isValidLocal = dbHelper.loginUser(email, pass)
        if (isValidLocal) {
            val user = dbHelper.getUser(email) ?: UserProfile(name = "Usuario", email = email)
            return@withContext Resource.Success(user)
        } else {
            return@withContext Resource.Error("Credenciales incorrectas. Verifica tu correo y contraseña.")
        }
    }

    suspend fun register(name: String, email: String, pass: String): Resource<Boolean> = withContext(Dispatchers.IO) {
        if (dbHelper.checkUser(email)) {
            return@withContext Resource.Error("El correo electrónico ya se encuentra registrado.")
        }
        val success = dbHelper.insertUser(name, email, pass)
        if (success) {
            Resource.Success(true)
        } else {
            Resource.Error("Error al guardar el usuario en la base de datos.")
        }
    }

    // --- Balance y Transacciones ---
    suspend fun getBalance(email: String): Resource<Double> = withContext(Dispatchers.IO) {
        val user = dbHelper.getUser(email)
        if (user != null) {
            Resource.Success(user.balance)
        } else {
            Resource.Success(124.57)
        }
    }

    suspend fun getTransactions(email: String): Resource<List<TransactionItem>> = withContext(Dispatchers.IO) {
        // Primero intentamos sincronizar con Retrofit si hay red
        try {
            val user = dbHelper.getUser(email)
            val response = apiService.getTransactions(user?.token)
            if (response.isSuccessful && response.body() != null) {
                val listDto = response.body()!!
                for (dto in listDto) {
                    dbHelper.insertTransaction(dto.description, dto.date, dto.amount, dto.type, email)
                }
            }
        } catch (e: Exception) {
            // Continúa en modo offline con datos locales
        }

        // Obtener historial desde la base de datos local (Room/SQLite)
        val localList = dbHelper.getTransactions(email)
        Resource.Success(localList)
    }

    suspend fun sendMoney(email: String, recipient: String, amount: Double, note: String): Resource<String> = withContext(Dispatchers.IO) {
        if (amount <= 0) {
            return@withContext Resource.Error("El monto a transferir debe ser mayor a 0.")
        }

        val user = dbHelper.getUser(email)
        val currentBalance = user?.balance ?: 124.57
        if (amount > currentBalance) {
            return@withContext Resource.Error("Saldo insuficiente. Tu saldo actual es \$${String.format(Locale.US, "%.2f", currentBalance)}.")
        }

        val newBalance = currentBalance - amount
        dbHelper.updateBalance(email, newBalance)

        val dateStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date())
        val title = if (recipient.isNotBlank()) recipient else "Transferencia enviada"
        dbHelper.insertTransaction(title, dateStr, amount, "EXPENSE", email)

        // Intento asíncrono con Retrofit
        try {
            apiService.createTransaction(
                user?.token,
                TransactionRequest(amount, "$title: $note", "EXPENSE")
            )
        } catch (ignored: Exception) {}

        Resource.Success("Transferencia realizada con éxito a $recipient")
    }

    suspend fun requestMoney(email: String, sender: String, amount: Double, note: String): Resource<String> = withContext(Dispatchers.IO) {
        if (amount <= 0) {
            return@withContext Resource.Error("El monto a ingresar debe ser mayor a 0.")
        }

        val user = dbHelper.getUser(email)
        val currentBalance = user?.balance ?: 124.57
        val newBalance = currentBalance + amount
        dbHelper.updateBalance(email, newBalance)

        val dateStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date())
        val title = if (sender.isNotBlank()) sender else "Depósito de dinero"
        dbHelper.insertTransaction(title, dateStr, amount, "DEPOSIT", email)

        // Intento asíncrono con Retrofit
        try {
            apiService.createTransaction(
                user?.token,
                TransactionRequest(amount, "$title: $note", "DEPOSIT")
            )
        } catch (ignored: Exception) {}

        Resource.Success("Depósito de fondos realizado con éxito")
    }
}
