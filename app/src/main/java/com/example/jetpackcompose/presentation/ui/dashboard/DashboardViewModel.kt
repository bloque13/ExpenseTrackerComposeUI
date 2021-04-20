package com.example.jetpackcompose.presentation.ui.dashboard

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.domain.model.Account
import com.example.jetpackcompose.domain.model.Transaction
import com.example.jetpackcompose.interactors.accounts.GetAccountsUseCase
import com.example.jetpackcompose.interactors.prepopulate.PrePopulateUseCase
import com.example.jetpackcompose.interactors.transactions.DeleteTransactionUseCase
import com.example.jetpackcompose.presentation.components.account.AccountListEvent
import com.example.jetpackcompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject
constructor(
    private val getAccounts: GetAccountsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val prePopulateUseCase: PrePopulateUseCase,
) : ViewModel() {
    val accounts: MutableState<List<Account>> = mutableStateOf(ArrayList())
    val transactions: MutableState<List<Transaction>> = mutableStateOf(ArrayList())
    val loading = mutableStateOf(false)

    init {
        sendEvent(AccountListEvent.LoadEvent)
    }

    fun sendEvent(event: AccountListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is AccountListEvent.LoadEvent -> {
                        loadAccounts()
                    }
                    is AccountListEvent.PrePopulateDbEvent -> {
                        prepopulateAccounts()
                    }
                    is AccountListEvent.TransactionDeletedEvent -> {
                        deleteTransaction(transactionId = event.transactionId)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            } finally {
                Log.d(TAG, "launchJob: finally called.")
            }
        }
    }

    private fun deleteTransaction(transactionId: Int) {
        accounts.value = emptyList()
        deleteTransactionUseCase.execute(transactionId = transactionId).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { _ ->
                sendEvent(AccountListEvent.LoadEvent)
            }
            dataState.error?.let { error ->
                Log.e(TAG, "delete transaction error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun prepopulateAccounts() {
        prePopulateUseCase.execute().onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { _ ->
                sendEvent(AccountListEvent.LoadEvent)
            }
            dataState.error?.let { error ->
                Log.e(TAG, "accounts error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun loadAccounts() {
        accounts.value = emptyList()
        transactions.value = emptyList()

        getAccounts.execute().onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list ->
                if (list.isEmpty()) {
                    sendEvent(AccountListEvent.PrePopulateDbEvent)
                } else {
                    accounts.value = list
                    prepForView()
                }
            }
            dataState.error?.let { error ->
                Log.e(TAG, "accounts error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun prepForView() {
        val trans = mutableListOf<Transaction>()
        accounts.value.forEach {
            trans.addAll(it.transactions)
        }
        transactions.value = trans
    }

    fun reload() {
        sendEvent(AccountListEvent.LoadEvent)
    }

}