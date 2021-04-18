package com.example.jetpackcompose.cache.model

import com.example.jetpackcompose.domain.model.Transaction
import com.example.jetpackcompose.domain.model.TransactionCategory
import com.example.jetpackcompose.domain.util.DomainMapper
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class TransactionEntityMapper : DomainMapper<TransactionEntity, Transaction> {

    private lateinit var cats: List<CategoryEntity>

    override fun mapToDomainModel(model: TransactionEntity): Transaction {

        return Transaction(
            id = model.id,
            accountId = model.accountId,
            dateCreated = Date(model.dateCreated),
            amount = BigDecimal(model.amount).setScale(2, RoundingMode.HALF_EVEN).toDouble(),
            type = model.type,
            category = getTranstionCategory(model.categoryId)
        )
    }

    private fun getTranstionCategory(categoryId: Int): TransactionCategory {
        val category = cats.find{ it.id == categoryId }
        return TransactionCategory(id = category!!.id, name = category.name, transactionType = category.transactionType)
    }

    override fun mapFromDomainModel(domainModel: Transaction): TransactionEntity {
        return TransactionEntity(
            id = domainModel.id,
            accountId = domainModel.accountId,
            dateCreated = domainModel.dateCreated.time,
            amount = domainModel.amount,
            type = domainModel.type,
            categoryId = domainModel.category.id
        )
    }

    fun fromEntityList(initial: List<TransactionEntity>, categories: List<CategoryEntity>): List<Transaction> {
        cats = categories
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Transaction>): List<TransactionEntity> {
        return initial.map { mapFromDomainModel(it) }
    }

}