package com.example.jetpackcompose.cache.database

import androidx.room.TypeConverter
import com.example.jetpackcompose.domain.model.TransactionType

class Converter {

    @TypeConverter
    fun fromTransactionType(transactionType: TransactionType): String {
        return transactionType.name
    }

    @TypeConverter
    fun toTransactionType(transactionType: String): TransactionType {
        return TransactionType.valueOf(transactionType)
    }

}