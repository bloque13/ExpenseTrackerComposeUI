package com.example.jetpackcompose.interactors.prepopulate

import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.model.AccountEntity
import com.example.jetpackcompose.cache.model.CategoryEntity
import com.example.jetpackcompose.cache.model.TransactionEntity
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.TransactionType.*
import com.example.jetpackcompose.util.ONE_DAY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextLong

class PrePopulateUseCase(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
) {
    fun execute(): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            insertAccounts()

            insertCategories()

            insertTransactions()

            emit(DataState.success(true))

        } catch (e: Exception) {
            emit(DataState.error<Boolean>(message = "Insert Data Error"))
        }
    }

    private suspend fun insertAccounts() {
        val accounts = mutableListOf<AccountEntity>()
        accounts.add(AccountEntity(id = 1, name = "Cash"))
        accounts.add(AccountEntity(id = 2, name = "Credit Card"))
        accounts.add(AccountEntity(id = 3, name = "Bank Account"))

        accountDao.insertAccounts(accounts)
    }

    private suspend fun insertCategories() {
        val categories = mutableListOf<CategoryEntity>()
        categories.add(CategoryEntity(id = 1, name = "Tax", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 2, name = "Grocery", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 3, name = "Entertainment", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 4, name = "Gym", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 5, name = "Health", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 6, name = "Salary", transactionType = "Income"))
        categories.add(CategoryEntity(id = 7, name = "Dividends", transactionType = "Income"))

        categoryDao.insertCategories(categories)
    }

    private suspend fun insertTransactions() {
        val transactions = mutableListOf<TransactionEntity>()

        repeat(12) {
            transactions.add(
                TransactionEntity(
                    id = it + 1,
                    accountId = 1,
                    amount = genAmount(),
                    categoryId = 3,
                    type = EXPENSE,
                    dateCreated = genDateCreated()
                )
            )
        }

        repeat(4) {
            transactions.add(
                TransactionEntity(
                    id = it + 13,
                    accountId = 2,
                    amount = genAmount(),
                    categoryId = 2,
                    type = EXPENSE,
                    dateCreated = genDateCreated()
                )
            )
        }

        repeat(2) { index ->
            transactions.add(
                TransactionEntity(
                    id = index + 15,
                    accountId = 3,
                    amount = genAmount(),
                    categoryId = 6,
                    type = INCOME,
                    dateCreated = if(index == 0) Date().time else Date().time - ONE_DAY
                )
            )
        }

        transactionDao.insertTransactions(transactions)
    }

    private fun genAmount() = nextDouble(10.0, 2000.00)

    private fun genDateCreated(): Long {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.YEAR, -1)
        return nextLong(cal.time.time, Date().time)
    }

}