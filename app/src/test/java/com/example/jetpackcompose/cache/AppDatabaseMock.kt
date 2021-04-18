package com.example.jetpackcompose.cache

import com.example.jetpackcompose.cache.model.AccountEntity
import com.example.jetpackcompose.cache.model.CategoryEntity
import com.example.jetpackcompose.cache.model.TransactionEntity

class AppDatabaseMock {

    val accounts = mutableListOf<AccountEntity>()

    val categories = mutableListOf<CategoryEntity>()

    val transactions = mutableListOf<TransactionEntity>()
}
