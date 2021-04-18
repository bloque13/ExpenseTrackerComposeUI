package com.example.jetpackcompose.interactors.prepopulate

import com.example.jetpackcompose.cache.AccountDaoMock
import com.example.jetpackcompose.cache.AppDatabaseMock
import com.example.jetpackcompose.cache.CategoryDaoMock
import com.example.jetpackcompose.cache.TransactionDaoMock
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.Account
import com.example.jetpackcompose.domain.model.TransactionCategory
import com.example.jetpackcompose.interactors.accounts.GetAccountsUseCase
import com.example.jetpackcompose.interactors.categories.GetCategoriesUseCase
import com.example.jetpackcompose.interactors.transactions.AddTransactionUseCase
import com.example.jetpackcompose.interactors.transactions.DeleteTransactionUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PrePopulateUseCaseTest {

    private val db = AppDatabaseMock()
    private lateinit var prePopulateUseCase: PrePopulateUseCase
    private lateinit var getAccountsUseCase: GetAccountsUseCase
    private lateinit var getCategoriesUseCae: GetCategoriesUseCase
    private lateinit var deleteTransactionUseCase: DeleteTransactionUseCase
    private lateinit var addTransactionUseCase: AddTransactionUseCase


    private lateinit var accountDao: AccountDaoMock
    private lateinit var categoryDao: CategoryDaoMock
    private lateinit var transactionDao: TransactionDaoMock

    private val accountMapper = AccountEntityMapper()
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

        getAccountsUseCase = GetAccountsUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao,
            accountMapper = accountMapper,
            transactionMapper = transactionMapper
        )

        getCategoriesUseCae = GetCategoriesUseCase(
            categoryDao = categoryDao,
            entityMapper = categoryEntityMapper,
        )

        deleteTransactionUseCase = DeleteTransactionUseCase(
            transactionDao = transactionDao,
        )

        addTransactionUseCase = AddTransactionUseCase(
            transactionDao = transactionDao,
            accountDao = accountDao,
            categoryDao = categoryDao,
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

        val flowItems2 = getAccountsUseCase.execute().toList()
        assert(flowItems2[0].loading)

        assertFlowDataIsListOfAccounts(flowItems2)
        assertAccountsMatch(flowItems2)

        assert(!flowItems2[1].loading)
    }

    @Test
    fun test_get_categories_after_prepopulated(): Unit = runBlocking {
        assert(categoryDao.getCategories().isEmpty())

        val flowItems = prePopulateUseCase.execute().toList()

        assert(categoryDao.getCategories().isNotEmpty())
        assert(flowItems[0].loading)
        assertThat(flowItems[1].data, equalTo(true))
        assert(!flowItems[1].loading)

        assert(categoryDao.getCategories().isNotEmpty())

        val flowItems2 = getCategoriesUseCae.execute().toList()
        assert(flowItems2[0].loading)

        assertFlowDataIsListOfCategories(flowItems2)

        assertCategoriesMatch(flowItems2)

        assert(!flowItems2[1].loading)
    }


    @Test
    fun test_transactions_added(): Unit = runBlocking {
        assert(transactionDao.getTransactions().isEmpty())

        val flowItems = prePopulateUseCase.execute().toList()

        assert(flowItems[0].loading)
        assertThat(flowItems[1].data, equalTo(true))
        assert(!flowItems[1].loading)

        val transactions = transactionDao.getTransactions()
        assert(transactions.isNotEmpty())
        assertThat(transactions.size, equalTo(18) )
    }


    @Test
    fun test_delete_transaction(): Unit = runBlocking {
        assert(transactionDao.getTransactions().isEmpty())
        prePopulateUseCase.execute().toList()

        val transactions = transactionDao.getTransactions()
        assertThat(transactions.size, equalTo(18))

        deleteTransactionUseCase.execute(transactionId = 1).toList()

        assertThat(transactionDao.getTransactions().size, equalTo(17))

    }



    private fun assertFlowDataIsListOfAccounts(flowItems: List<DataState<List<Account>>>) {
        val accounts = flowItems[1].data
        assert(accounts?.size ?: 0 > 0)
    }

    private fun assertAccountsMatch(flowItems: List<DataState<List<Account>>>) {
        val accounts = flowItems[1].data
        assertThat(accounts?.get(index = 0)?.name, equalTo("Cash"))
        assertThat(accounts?.get(index = 1)?.name, equalTo("Credit Card"))
        assertThat(accounts?.get(index = 2)?.name, equalTo("Bank Account"))
    }

    private fun assertFlowDataIsListOfCategories(flowItems: List<DataState<List<TransactionCategory>>>) {
        val categories = flowItems[1].data
        assert(categories?.size ?: 0 > 0)
    }

    private fun assertCategoriesMatch(flowItems: List<DataState<List<TransactionCategory>>>) {
        val accounts = flowItems[1].data
        assertThat(accounts?.get(index = 0)?.name, equalTo("Tax"))
        assertThat(accounts?.get(index = 1)?.name, equalTo("Grocery"))
        assertThat(accounts?.get(index = 2)?.name, equalTo("Entertainment"))
        assertThat(accounts?.get(index = 3)?.name, equalTo("Gym"))
        assertThat(accounts?.get(index = 4)?.name, equalTo("Health"))
        assertThat(accounts?.get(index = 5)?.name, equalTo("Salary"))
        assertThat(accounts?.get(index = 6)?.name, equalTo("Dividends"))

        assertThat(accounts?.get(index = 0)?.transactionType, equalTo("Expense"))
        assertThat(accounts?.get(index = 1)?.transactionType, equalTo("Expense"))
        assertThat(accounts?.get(index = 2)?.transactionType, equalTo("Expense"))
        assertThat(accounts?.get(index = 3)?.transactionType, equalTo("Expense"))
        assertThat(accounts?.get(index = 4)?.transactionType, equalTo("Expense"))
        assertThat(accounts?.get(index = 5)?.transactionType, equalTo("Income"))
        assertThat(accounts?.get(index = 6)?.transactionType, equalTo("Income"))
    }

}