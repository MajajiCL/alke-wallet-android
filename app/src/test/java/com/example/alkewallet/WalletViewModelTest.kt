package com.example.alkewallet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alkewallet.data.model.Resource
import com.example.alkewallet.data.model.TransactionItem
import com.example.alkewallet.data.repository.WalletRepository
import com.example.alkewallet.ui.viewmodel.WalletViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: WalletRepository
    private lateinit var viewModel: WalletViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(WalletRepository::class.java)
        viewModel = WalletViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun sendMoney_withInvalidAmount_setsErrorState() {
        viewModel.sendMoney("test@email.com", "Yara", "-5.0", "nota")

        val state = viewModel.operationStatus.value
        assertTrue(state is Resource.Error)
        assertEquals("Ingresa un monto numérico válido mayor a $0.", (state as Resource.Error).message)
    }

    @Test
    fun sendMoney_withZeroAmount_setsErrorState() {
        viewModel.sendMoney("test@email.com", "Yara", "0.0", "nota")

        val state = viewModel.operationStatus.value
        assertTrue(state is Resource.Error)
        assertEquals("Ingresa un monto numérico válido mayor a $0.", (state as Resource.Error).message)
    }

    @Test
    fun requestMoney_withEmptyAmount_setsErrorState() {
        viewModel.requestMoney("test@email.com", "Reem", "", "nota")

        val state = viewModel.operationStatus.value
        assertTrue(state is Resource.Error)
    }

    @Test
    fun loadWalletData_updatesBalanceAndTransactions() = runTest(testDispatcher) {
        val email = "amanda@alkewallet.com"
        val expectedBalance = 250.0
        val mockTransactions = listOf(
            TransactionItem(1, "Depósito", "Oct 14", 50.0, "DEPOSIT")
        )

        `when`(repository.getBalance(email)).thenReturn(Resource.Success(expectedBalance))
        `when`(repository.getTransactions(email)).thenReturn(Resource.Success(mockTransactions))

        viewModel.loadWalletData(email)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expectedBalance, viewModel.balance.value)
        assertEquals(mockTransactions, viewModel.transactions.value)
    }
}
