package com.example.jetpackcompose.di

import androidx.room.Room
import com.example.jetpackcompose.cache.AccountDao
import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.TransactionDao
import com.example.jetpackcompose.cache.database.AppDatabase
import com.example.jetpackcompose.cache.model.AccountEntityMapper
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.cache.model.TransactionEntityMapper
import com.example.jetpackcompose.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

  @Singleton
  @Provides
  fun provideDb(app: BaseApplication): AppDatabase {
    return Room
      .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
      .fallbackToDestructiveMigration()
      .build()
  }

  @Singleton
  @Provides
  fun provideAccountDao(db: AppDatabase): AccountDao {
    return db.accountDao()
  }

  @Singleton
  @Provides
  fun provideCategoryDao(db: AppDatabase): CategoryDao {
    return db.categoryDao()
  }

  @Singleton
  @Provides
  fun provideTransactionDao(db: AppDatabase): TransactionDao {
    return db.transactionDao()
  }

  @Singleton
  @Provides
  fun provideAccountEntityMapper(): AccountEntityMapper{
    return AccountEntityMapper()
  }

  @Singleton
  @Provides
  fun provideTransactionEntityMapper(): TransactionEntityMapper {
    return TransactionEntityMapper()
  }

  @Singleton
  @Provides
  fun provideCategoryEntityMapper(): CategoryEntityMapper{
    return CategoryEntityMapper()
  }

}