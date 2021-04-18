package com.example.jetpackcompose.interactors.transaction

import com.example.jetpackcompose.cache.AccountDaoMock
import com.example.jetpackcompose.cache.AppDatabaseMock
import com.example.jetpackcompose.cache.CategoryDaoMock
import com.example.jetpackcompose.cache.TransactionDaoMock
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
import com.example.jetpackcompose.interactors.accounts.GetAccountsUseCase
import com.example.jetpackcompose.interactors.prepopulate.PrePopulateUseCase
import com.example.jetpackcompose.interactors.categories.GetCategoriesUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddTransactionUseCaseTest {

    private val db = AppDatabaseMock()
    private lateinit var prePopulateUseCase: PrePopulateUseCase
    private lateinit var getAccounts: GetAccountsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    private lateinit var accountDao: AccountDaoMock
    private lateinit var categoryDao: CategoryDaoMock
    private lateinit var transactionDao: TransactionDaoMock
    private val entityMapper = AccountEntityMapper()
    private val transactionMapper = TransactionEntityMapper()
    private val categoryEntityMapper = CategoryEntityMapper()

    @BeforeEach
    fun setup() {
        accountDao = AccountDaoMock(db = db)
        categoryDao = CategoryDaoMock(db = db)
        transactionDao = TransactionDaoMock(db = db)

        prePopulateUseCase = PrePopulateUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao
        )

        getAccounts = GetAccountsUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            accountMapper = entityMapper,
            transactionMapper = transactionMapper,
            transactionDao = transactionDao
        )

        getCategoriesUseCase = GetCategoriesUseCase(
            categoryDao = categoryDao,
            entityMapper = categoryEntityMapper,
        )
    }

    @Test
    fun test_get_accounts_after_prepopulated(): Unit = runBlocking {
        assert(accountDao.getAccounts().isEmpty())

        val flowItems = prePopulateUseCase.execute().toList()

        assert(accountDao.getAccounts().isNotEmpty())
        assert(flowItems[0].loading)
        assertThat(flowItems[1].data, equalTo(true))
        assert(!flowItems[1].loading)

        assert(accountDao.getAccounts().isNotEmpty())
        assert(transactionDao.getTransactions().isNotEmpty())

    }

}