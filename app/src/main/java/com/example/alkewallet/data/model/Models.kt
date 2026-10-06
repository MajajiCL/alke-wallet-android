package com.example.alkewallet.data.model

import com.google.gson.annotations.SerializedName

// --- Modelos de Dominio ---
data class UserProfile(
    val id: Int = 1,
    val name: String,
    val email: String,
    val balance: Double = 124.57,
    val photoUrl: String? = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400",
    val token: String? = null
)

data class TransactionItem(
    val id: Int,
    val title: String,
    val date: String,
    val amount: Double,
    val type: String, // "DEPOSIT" o "EXPENSE"
    val avatarName: String? = null
) {
    val isDeposit: Boolean get() = type.equals("DEPOSIT", ignoreCase = true)
    val formattedAmount: String get() = if (isDeposit) String.format("+$%.2f", amount) else String.format("-$%.2f", amount)
}

// --- Wrapper para Estados de Red / UI ---
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

// --- DTOs para Retrofit (API REST Externa) ---
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)

data class BalanceDto(
    @SerializedName("balance") val balance: Double,
    @SerializedName("currency") val currency: String = "USD"
)

data class TransactionRequest(
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String
)

data class TransactionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String
)
