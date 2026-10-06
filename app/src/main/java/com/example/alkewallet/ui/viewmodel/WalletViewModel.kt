package com.example.alkewallet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.model.TransactionItem
import com.example.alkewallet.data.repository.WalletRepository
import kotlinx.coroutines.launch

class WalletViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _transactions = MutableLiveData<List<TransactionItem>>()
    val transactions: LiveData<List<TransactionItem>> get() = _transactions

    private val _operationStatus = MutableLiveData<Resource<String>>()
    val operationStatus: LiveData<Resource<String>> get() = _operationStatus

    fun loadWalletData(email: String) {
        viewModelScope.launch {
            val balRes = repository.getBalance(email)
            if (balRes is Resource.Success) {
                _balance.value = balRes.data
            }

            val txRes = repository.getTransactions(email)
            if (txRes is Resource.Success) {
                _transactions.value = txRes.data
            }
        }
    }

    fun sendMoney(email: String, recipient: String, amountStr: String, note: String) {
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _operationStatus.value = Resource.Error("Ingresa un monto numérico válido mayor a \$0.")
            return
        }

        _operationStatus.value = Resource.Loading
        viewModelScope.launch {
            val res = repository.sendMoney(email, recipient, amount, note)
            _operationStatus.value = res
            if (res is Resource.Success) {
                loadWalletData(email)
            }
        }
    }

    fun requestMoney(email: String, sender: String, amountStr: String, note: String) {
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _operationStatus.value = Resource.Error("Ingresa un monto numérico válido mayor a \$0.")
            return
        }

        _operationStatus.value = Resource.Loading
        viewModelScope.launch {
            val res = repository.requestMoney(email, sender, amount, note)
            _operationStatus.value = res
            if (res is Resource.Success) {
                loadWalletData(email)
            }
        }
    }
}
