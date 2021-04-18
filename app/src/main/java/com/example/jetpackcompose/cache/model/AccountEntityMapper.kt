package com.example.jetpackcompose.cache.model

import com.example.jetpackcompose.domain.model.Account
import com.example.jetpackcompose.domain.util.DomainMapper

class AccountEntityMapper : DomainMapper<AccountEntity, Account> {

    override fun mapToDomainModel(model: AccountEntity): Account {
        return Account(
            id = model.id,
            name = model.name,
        )
    }

    override fun mapFromDomainModel(domainModel: Account): AccountEntity {
        return AccountEntity(
            id = domainModel.id,
            name = domainModel.name,
            )
    }

    fun fromEntityList(initial: List<AccountEntity>): List<Account> {
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Account>): List<AccountEntity> {
        return initial.map { mapFromDomainModel(it) }
    }



}