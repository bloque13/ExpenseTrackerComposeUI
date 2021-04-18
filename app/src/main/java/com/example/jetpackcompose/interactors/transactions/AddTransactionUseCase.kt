package com.example.jetpackcompose.interactors.transactions

import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.model.TransactionEntity
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class AddTransactionUseCase(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
) {
    fun execute(
        selectedAccount: String,
        selectedCategory: String,
        transactionAmount: Double,
        transactionType: TransactionType
    ): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            var accountId: Int = -1
            var categoryId: Int = -1

            accountDao.getAccountByName(name = selectedAccount)?.let {
                accountId = it.id
            }

            categoryDao.getCategoryByName(name = selectedCategory)?.let {
                categoryId = it.id
            }

            transactionDao.insertTransaction(
                transaction = TransactionEntity(
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = transactionAmount,
                    type = transactionType,
                    dateCreated = Date().time
                )
            )

            emit(DataState.success(true))

        } catch (e: Exception) {
            emit(DataState.error<Boolean>(message = "Insert Data Error"))
        }
    }


}