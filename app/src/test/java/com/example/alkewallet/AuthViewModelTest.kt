package com.example.alkewallet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: WalletRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        repository = mock(WalletRepository::class.java)
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun login_withBlankFields_setsErrorState() {
        viewModel.login("", "")

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("Por favor completa todos los campos.", (state as Resource.Error).message)
    }

    @Test
    fun register_withPasswordMismatch_setsErrorState() {
        viewModel.register("Amanda", "amanda@test.com", "1234", "9999")

        val state = viewModel.registerState.value
        assertTrue(state is Resource.Error)
        assertEquals("Las contraseñas no coinciden.", (state as Resource.Error).message)
    }

    @Test
    fun register_withEmptyFields_setsErrorState() {
        viewModel.register("", "", "", "")

        val state = viewModel.registerState.value
        assertTrue(state is Resource.Error)
        assertEquals("Por favor llena todos los campos obligatorios.", (state as Resource.Error).message)
    }
}
