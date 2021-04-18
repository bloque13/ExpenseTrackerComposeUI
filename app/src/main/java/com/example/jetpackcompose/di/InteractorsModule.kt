package com.example.jetpackcompose.di

import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
import com.example.jetpackcompose.interactors.transactions.AddTransactionUseCase
import com.example.jetpackcompose.interactors.accounts.GetAccountsUseCase
import com.example.jetpackcompose.interactors.prepopulate.PrePopulateUseCase
import com.example.jetpackcompose.interactors.categories.GetCategoriesUseCase
import com.example.jetpackcompose.interactors.transactions.DeleteTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun providePrePopulate(
        accountDao: AccountDao,
        categoryDao: CategoryDao,
        transactionDao: TransactionDao,
    ): PrePopulateUseCase {
        return PrePopulateUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao,
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetAccountsUseCase(
      accountDao: AccountDao,
      transactionDao: TransactionDao,
      categoryDao: CategoryDao,
      accountMapper: AccountEntityMapper,
      transactionMapper: TransactionEntityMapper
    ): GetAccountsUseCase {
        return GetAccountsUseCase(
            accountDao = accountDao,
            transactionDao = transactionDao,
            categoryDao = categoryDao,
            accountMapper = accountMapper,
            transactionMapper = transactionMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetCategoriesUseCase(
        categoryDao: CategoryDao,
        categoryEntityMapper: CategoryEntityMapper,
    ): GetCategoriesUseCase {
        return GetCategoriesUseCase(
            categoryDao = categoryDao,
            entityMapper = categoryEntityMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideAddTransactionUseCase(
        categoryDao: CategoryDao,
        accountDao: AccountDao,
        transactionDao: TransactionDao,
    ): AddTransactionUseCase {
        return AddTransactionUseCase(
            accountDao = accountDao,
            categoryDao = categoryDao,
            transactionDao = transactionDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideDeleteTransactionUseCase(
        transactionDao: TransactionDao,
    ): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(
            transactionDao = transactionDao,
        )
    }

}