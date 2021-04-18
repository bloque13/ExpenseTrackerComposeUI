package com.example.jetpackcompose.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.model.AccountEntity
import com.example.jetpackcompose.cache.model.CategoryEntity
import com.example.jetpackcompose.cache.model.TransactionEntity

@Database(entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    companion object{
        val DATABASE_NAME: String = "transactions_db"
    }
}