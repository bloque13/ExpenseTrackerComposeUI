package com.example.jetpackcompose.presentation.ui.transaction

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.domain.model.Account
import com.example.jetpackcompose.domain.model.TransactionCategory
import com.example.jetpackcompose.domain.model.TransactionType
import com.example.jetpackcompose.interactors.accounts.GetAccountsUseCase
import com.example.jetpackcompose.interactors.categories.GetCategoriesUseCase
import com.example.jetpackcompose.interactors.transactions.AddTransactionUseCase
import com.example.jetpackcompose.presentation.components.transaction.TransactionEvent
import com.example.jetpackcompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel
@Inject
constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
) : ViewModel() {
    val transactionAmount: MutableState<Double> = mutableStateOf(0.0)
    val selectedCategory: MutableState<String> = mutableStateOf("Please Select")
    val selectedCategoryMap: MutableState<Map<Int, String>> = mutableStateOf(emptyMap())
    val selectedAccount: MutableState<String> = mutableStateOf("Please Select")
    val isIncomeSelected = mutableStateOf(true)
    val confirmTransaction = mutableStateOf(false)
    val accounts: MutableState<List<Account>> = mutableStateOf(ArrayList())
    val categories: MutableState<List<TransactionCategory>> = mutableStateOf(ArrayList())
    val loading = mutableStateOf(false)
    val isChangeListenerActive = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")
    val transactionSaved: MutableState<Boolean> = mutableStateOf(false)
    val amountValueEntered: MutableState<String> = mutableStateOf("")

    init {
        sendEvent(TransactionEvent.LoadEvent)
    }

    fun sendEvent(event: TransactionEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is TransactionEvent.LoadEvent -> {
                        loadAccounts()
                        loadCategories()
                    }
                    is TransactionEvent.SaveTransactionEvent -> {
                        accounts.value = emptyList()
                        addTransaction()
                    }
                    is TransactionEvent.TransactionSavedEvent -> {
                        transactionSaved.value = true
                    }
                    is TransactionEvent.TransactionConfirmEvent -> {
                        confirmTransaction.value = true
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

    private fun addTransaction() {
        accounts.value = emptyList()
        addTransactionUseCase.execute(
            selectedAccount = selectedAccount.value,
            selectedCategory = selectedCategory.value,
            transactionAmount = transactionAmount.value,
            transactionType = if (isIncomeSelected.value) TransactionType.INCOME else TransactionType.EXPENSE
        ).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { _ ->
                sendEvent(TransactionEvent.TransactionSavedEvent)
            }
            dataState.error?.let { error ->
                Log.e(TAG, "add transaction error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun loadAccounts() {
        accounts.value = emptyList()
        getAccountsUseCase.execute().onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list ->
                accounts.value = list
            }
            dataState.error?.let { error ->
                Log.e(TAG, "accounts error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCategories() {
        getCategoriesUseCase.execute().onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list ->
                categories.value = list
                updateCategoryMap()
            }
            dataState.error?.let { error ->
                Log.e(TAG, "categories error: ${error}")
            }
        }.launchIn(viewModelScope)
    }

    fun validate() {
        errorMessage.value = ""
        if (selectedAccount.value == "Please Select") {
            errorMessage.value = "Please select an account"
            return
        } else if (selectedCategory.value == "Please Select") {
            val expenseType =
                if (isIncomeSelected.value) TransactionType.INCOME.value else TransactionType.EXPENSE.value
            errorMessage.value = "Please select a $expenseType category"
            return
        } else if (transactionAmount.value == 0.0) {
            errorMessage.value = "Please enter an amount"
            return
        }
        sendEvent(TransactionEvent.TransactionConfirmEvent)
    }

    fun onAmountEnteredChange(change: String) {

        isChangeListenerActive.value = false

        val cleanString: String = change.replace("""[$,._-]""".toRegex(), "")
        val parsed = cleanString.toDouble()
        transactionAmount.value = parsed / 100

        amountValueEntered.value = "%.2f".format(parsed / 100)

        isChangeListenerActive.value = true
    }

    fun saveTransaction() {
        sendEvent(TransactionEvent.SaveTransactionEvent)
    }

    fun selectedTransactionType(it: TransactionType) {
        isIncomeSelected.value = (it == TransactionType.INCOME)
        updateCategoryMap()
        selectedCategory.value = "Please Select"
    }

    private fun updateCategoryMap() {
        selectedCategoryMap.value =
            categories.value.filter { it.transactionType == (if (isIncomeSelected.value) "Income" else "Expense") }
                .map { it.id to it.name }.toMap()
    }

}