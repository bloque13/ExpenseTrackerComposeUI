package com.example.jetpackcompose.interactors.accounts

import com.example.jetpackcompose.cache.*
import com.example.jetpackcompose.cache.model.AccountEntity
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
//import com.example.jetpackcompose.cache.model.AccountTransactions
//import com.example.jetpackcompose.cache.model.TransactionEntity
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.Account
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetAccountsUseCaseTest {

    private val db = AppDatabaseMock()
    private lateinit var getAccounts: GetAccountsUseCase
    private lateinit var accountDao: AccountDaoMock
    private lateinit var categoryDao: CategoryDaoMock
    private lateinit var transactionDao: TransactionDao
    private val accountMapper = AccountEntityMapper()
    private val transactionMapper = TransactionEntityMapper()

    @BeforeEach
    fun setup() {
        accountDao = AccountDaoMock(db = db)
        categoryDao = CategoryDaoMock(db = db)
        transactionDao = TransactionDaoMock(db = db)
        getAccounts = GetAccountsUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao,
            accountMapper = accountMapper,
            transactionMapper = transactionMapper
        )
    }

    @Test
    fun test_get_accounts_empty(): Unit = runBlocking {
        assert(accountDao.getAccounts().isEmpty())

        val flowItems = getAccounts.execute().toList()

        assert(accountDao.getAccounts().isEmpty())
        assertLoadingEmissionShown(flowItems)
        assertFlowDataIsEmptyListOfAccounts(flowItems)
        assertLoadingEmissionNotShown(flowItems)
    }

    @Test
    fun test_get_accounts_with_test_account_data(): Unit = runBlocking {
        assert(accountDao.getAccounts().isEmpty())

        addATestAccountsToDb()

        val flowItems = getAccounts.execute().toList()

        assert(accountDao.getAccounts().isNotEmpty())
        assertLoadingEmissionShown(flowItems)
        assertFlowDataIsListOfAccounts(flowItems)
        assertFirstAccountMatches(flowItems)
        assertLoadingEmissionNotShown(flowItems)
    }

    @Test
    fun test_get_accounts_with_test_account_and_transaction_data(): Unit = runBlocking {
        assert(accountDao.getAccounts().isEmpty())

        addATestAccountWithTransactionsToDb()

        val flowItems = getAccounts.execute().toList()

        assert(accountDao.getAccounts().isNotEmpty())
        assertLoadingEmissionShown(flowItems)
        assertFlowDataIsListOfAccounts(flowItems)
        assertFirstAccountMatches(flowItems)
        assertLoadingEmissionNotShown(flowItems)
    }

    private fun addATestAccountsToDb() {
        db.accounts.add(AccountEntity(id = 1, name = "Cash"))
        db.accounts.add(AccountEntity(id = 2, name = "Credit Card"))
        db.accounts.add(AccountEntity(id = 3, name = "Bank Account"))
    }

    private fun addATestAccountWithTransactionsToDb() {
//        val transaction =
//            TransactionEntity(id = 1, accountId = 1, type = "", description = "Lunch", 21.00)

        val account = AccountEntity(id = 1, name = "Cash")
        db.accounts.add(account)

//        db.transactions.add(AccountTransactions(account = account, transactions = listOf(transaction) ))
    }

    private fun assertFlowDataIsListOfAccounts(flowItems: List<DataState<List<Account>>>) {
        val accounts = flowItems[1].data
        assert(accounts?.size ?: 0 > 0)
    }

    private fun assertFlowDataIsEmptyListOfAccounts(flowItems: List<DataState<List<Account>>>) {
        val accounts = flowItems[1].data
        assertThat(accounts?.size, equalTo(0))
    }

    private fun assertFirstAccountMatches(flowItems: List<DataState<List<Account>>>) {
        val accounts = flowItems[1].data
        val account = accounts?.get(index = 0)
        assert(account is Account)
        assertThat(account?.name, equalTo("Cash"))
    }

    private fun assertLoadingEmissionShown(flowItems: List<DataState<List<Account>>>) {
        assert(flowItems[0].loading)
    }

    private fun assertLoadingEmissionNotShown(flowItems: List<DataState<List<Account>>>) {
        assert(! flowItems[1].loading)
    }

}