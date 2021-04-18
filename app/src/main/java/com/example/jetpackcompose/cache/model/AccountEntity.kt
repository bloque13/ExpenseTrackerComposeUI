package com.example.jetpackcompose.cache.model

import androidx.room.*

@Entity(tableName = "accounts")
data class AccountEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String,

)
