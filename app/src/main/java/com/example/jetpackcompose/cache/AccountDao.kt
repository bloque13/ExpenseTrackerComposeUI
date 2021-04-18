package com.example.jetpackcompose.cache

import androidx.room.*
import com.example.jetpackcompose.cache.model.AccountEntity

@Dao
interface AccountDao {

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccounts(accounts: List<AccountEntity>): LongArray

    @Query("SELECT * FROM accounts")
    suspend fun getAccounts(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountEntity?

    @Query("SELECT * FROM accounts WHERE name = :name")
    suspend fun getAccountByName(name: String): AccountEntity?

    @Query("DELETE FROM accounts")
    suspend fun deleteAllAccounts()

}