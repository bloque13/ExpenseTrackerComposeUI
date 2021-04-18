package com.example.jetpackcompose.cache

import androidx.room.*
import com.example.jetpackcompose.cache.model.AccountEntity
import com.example.jetpackcompose.cache.model.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>): LongArray

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): CategoryEntity?

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

}