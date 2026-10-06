package com.example.alkewallet.data.remote

import com.example.alkewallet.data.model.BalanceDto
import com.example.alkewallet.data.model.LoginRequest
import com.example.alkewallet.data.model.LoginResponse
import com.example.alkewallet.data.model.TransactionDto
import com.example.alkewallet.data.model.TransactionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("wallet/balance")
    suspend fun getBalance(
        @Header("Authorization") token: String? = null
    ): Response<BalanceDto>

    @GET("wallet/transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String? = null
    ): Response<List<TransactionDto>>

    @POST("wallet/transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String? = null,
        @Body request: TransactionRequest
    ): Response<TransactionDto>
}
