package com.example.jetpackcompose.cache

import com.example.jetpackcompose.cache.model.CategoryEntity

class CategoryDaoMock(
    private val db: AppDatabaseMock
): CategoryDao {

    override suspend fun insertCategory(category: CategoryEntity): Long {
        db.categories.add(category)
        return 1
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>): LongArray {
        db.categories.addAll(categories)
        return longArrayOf(1)
    }

    override suspend fun getCategories(): List<CategoryEntity> {
        return db.categories
    }

    override suspend fun getCategoryById(id: Int): CategoryEntity? {
        return db.categories.find { it.id == id }
    }

    override suspend fun getCategoryByName(name: String): CategoryEntity? {
        return db.categories.find { it.name == name }
    }

    override suspend fun deleteAllCategories() {
        db.accounts.clear()
    }

}