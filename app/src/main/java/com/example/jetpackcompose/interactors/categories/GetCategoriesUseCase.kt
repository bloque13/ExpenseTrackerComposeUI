package com.example.jetpackcompose.interactors.categories

import com.example.jetpackcompose.cache.CategoryDao
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCategoriesUseCase(
    private val categoryDao: CategoryDao,
    private val entityMapper: CategoryEntityMapper,
) {
    fun execute(): Flow<DataState<List<TransactionCategory>>> = flow {
        try {
            emit(DataState.loading())
            val cacheResult = categoryDao.getCategories()
            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<TransactionCategory>>(e.message ?: "Unknown Error"))
        }
    }
}