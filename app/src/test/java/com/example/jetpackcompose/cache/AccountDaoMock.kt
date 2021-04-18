package com.example.jetpackcompose.cache

import com.example.jetpackcompose.cache.model.AccountEntity

class AccountDaoMock(
    private val db: AppDatabaseMock
): AccountDao {

    override suspend fun insertAccount(account: AccountEntity): Long {
        db.accounts.add(account)
        return 1
    }

    override suspend fun insertAccounts(accounts: List<AccountEntity>): LongArray {
        db.accounts.addAll(accounts)
        return longArrayOf(1)
    }

    override suspend fun getAccounts(): List<AccountEntity> {
        return db.accounts
    }

    override suspend fun getAccountById(id: Int): AccountEntity? {
        return db.accounts.find { it.id == id }
    }

    override suspend fun getAccountByName(name: String): AccountEntity? {
        return db.accounts.find { it.name == name }
    }

    override suspend fun deleteAllAccounts() {
        db.accounts.clear()
    }

}