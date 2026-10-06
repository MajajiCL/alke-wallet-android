package com.example.alkewallet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.model.UserProfile
import com.example.alkewallet.data.repository.WalletRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<UserProfile>>()
    val loginState: LiveData<Resource<UserProfile>> get() = _loginState

    private val _registerState = MutableLiveData<Resource<Boolean>>()
    val registerState: LiveData<Resource<Boolean>> get() = _registerState

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _loginState.value = Resource.Error("Por favor completa todos los campos.")
            return
        }

        _loginState.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.login(email.trim(), pass.trim())
            _loginState.value = result
        }
    }

    fun register(name: String, email: String, pass: String, passRepeat: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _registerState.value = Resource.Error("Por favor llena todos los campos obligatorios.")
            return
        }
        if (pass != passRepeat) {
            _registerState.value = Resource.Error("Las contraseñas no coinciden.")
            return
        }

        _registerState.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.register(name.trim(), email.trim(), pass.trim())
            _registerState.value = result
        }
    }
}
