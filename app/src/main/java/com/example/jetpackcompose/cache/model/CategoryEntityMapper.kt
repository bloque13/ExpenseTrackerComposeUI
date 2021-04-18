package com.example.jetpackcompose.cache.model

import com.example.jetpackcompose.domain.model.TransactionCategory
import com.example.jetpackcompose.domain.util.DomainMapper

class CategoryEntityMapper : DomainMapper<CategoryEntity, TransactionCategory> {

    override fun mapToDomainModel(model: CategoryEntity): TransactionCategory {
        return TransactionCategory (
            id = model.id,
            name = model.name,
            transactionType = model.transactionType
        )
    }

    override fun mapFromDomainModel(domainModel: TransactionCategory): CategoryEntity {
        return CategoryEntity(
            id = domainModel.id,
            name = domainModel.name,
            transactionType = domainModel.transactionType
            )
    }

    fun fromEntityList(initial: List<CategoryEntity>): List<TransactionCategory> {
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<TransactionCategory>): List<CategoryEntity> {
        return initial.map { mapFromDomainModel(it) }
    }

}