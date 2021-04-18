package com.example.jetpackcompose.interactors.transactions

import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteTransactionUseCase(
    private val transactionDao: TransactionDao,
) {
    fun execute(transactionId: Int): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            transactionDao.deleteTransactionById(transactionId = transactionId)

            emit(DataState.success(true))

        } catch (e: Exception) {
            emit(DataState.error<Boolean>(e.message ?: "Unknown Error"))
        }
    }
}