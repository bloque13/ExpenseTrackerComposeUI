package com.example.jetpackcompose.domain.model

import java.util.*

data class Account(
    val id: Int,
    val name: String,
    var transactions: List<Transaction> = listOf()
)

data class Transaction(
    val id: Int,
    val accountId: Int,
    val category: TransactionCategory,
    val amount: Double,
    val dateCreated: Date,
    val type: TransactionType
)

data class TransactionCategory(
    val id: Int,
    val name: String,
    var transactionType: String,
)

enum class TransactionType(val value: String){
    INCOME("Income"),
    EXPENSE("Expense"),
}

fun getAllTransactionTypes(): List<TransactionType>{
    return listOf(TransactionType.INCOME, TransactionType.EXPENSE)
}

fun getTransactionType(value: String): TransactionType? {
    val map = TransactionType.values().associateBy(TransactionType::value)
    return map[value]
}

