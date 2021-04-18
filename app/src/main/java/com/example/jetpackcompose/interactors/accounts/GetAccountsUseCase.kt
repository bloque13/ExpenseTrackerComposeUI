package com.example.jetpackcompose.interactors.accounts

import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAccountsUseCase(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val accountMapper: AccountEntityMapper,
    private val transactionMapper: TransactionEntityMapper,
) {
    fun execute(): Flow<DataState<List<Account>>> = flow {
        try {
            emit(DataState.loading())

            val accounts = accountMapper.fromEntityList(
                accountDao.getAccounts()
            )

            accounts.forEach {
                it.transactions = transactionMapper.fromEntityList(
                    transactionDao.getTransactionsByAccountId(id = it.id),
                    categoryDao.getCategories()
                )
            }

            emit(DataState.success(accounts))

        } catch (e: Exception) {
            emit(DataState.error<List<Account>>(e.message ?: "Unknown Error"))
        }
    }
}