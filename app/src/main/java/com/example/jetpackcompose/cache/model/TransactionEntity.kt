package com.example.jetpackcompose.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.jetpackcompose.domain.model.TransactionType
import java.util.*

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "accountId")
    var accountId: Int,

    @ColumnInfo(name = "categoryId")
    var categoryId: Int,

    @ColumnInfo(name = "type")
    var type: TransactionType,

    @ColumnInfo(name = "amount")
    var amount: Double,

    @ColumnInfo(name = "dateCreated")
    var dateCreated: Long,

    )