package com.example.jetpackcompose.cache

import com.example.jetpackcompose.cache.model.TransactionEntity
import com.example.jetpackcompose.domain.model.Transaction

class TransactionDaoMock(
    private val db: AppDatabaseMock
): TransactionDao {

    override suspend fun insertTransaction(transaction: TransactionEntity): Long {
        db.transactions.add(transaction)
        return 1
    }

    override suspend fun insertTransactions(transactions: List<TransactionEntity>): LongArray {
        db.transactions.addAll(transactions)
        return longArrayOf(1)
    }

    override suspend fun getTransactions(): List<TransactionEntity> {
        return db.transactions
    }

    override suspend fun getTransactionById(id: Int): TransactionEntity? {
        return db.transactions.find { it.id == id }
    }

    override suspend fun getTransactionsByAccountId(id: Int): List<TransactionEntity> {
        return db.transactions.filter { it.accountId == id }
    }

    override suspend fun deleteAllTransactions() {
        db.transactions.clear()
    }

    override suspend fun deleteTransactionById(transactionId: Int) {
        val new = mutableListOf<TransactionEntity>()
        db.transactions.filterTo(new, { it.id != transactionId })
        db.transactions.clear()
        db.transactions.addAll(new)
    }

}