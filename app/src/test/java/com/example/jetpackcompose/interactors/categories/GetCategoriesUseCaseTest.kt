package com.example.jetpackcompose.interactors.categories

import com.example.jetpackcompose.cache.AppDatabaseMock
import com.example.jetpackcompose.cache.CategoryDaoMock
import com.example.jetpackcompose.cache.model.CategoryEntity
import com.example.jetpackcompose.cache.model.CategoryEntityMapper
import com.example.jetpackcompose.domain.data.DataState
import com.example.jetpackcompose.domain.model.TransactionCategory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetCategoriesUseCaseTest {
    private val db = AppDatabaseMock()
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var categoryDao: CategoryDaoMock
    private val entityMapper = CategoryEntityMapper()

    @BeforeEach
    fun setup() {
        categoryDao = CategoryDaoMock(db = db)
        getCategoriesUseCase = GetCategoriesUseCase(
            categoryDao = categoryDao,
            entityMapper = entityMapper,
        )
    }

    @Test
    fun test_get_categories_empty(): Unit = runBlocking {
        assert(categoryDao.getCategories().isEmpty())

        val flowItems = getCategoriesUseCase.execute().toList()

        assert(categoryDao.getCategories().isEmpty())
        assertLoadingEmissionShown(flowItems)
        assertFlowDataIsEmptyListOfAccounts(flowItems)
        assertLoadingEmissionNotShown(flowItems)
    }

    @Test
    fun test_get_categories_with_test_account_data(): Unit = runBlocking {
        assert(categoryDao.getCategories().isEmpty())

        addATestCategoriesToDb()

        val flowItems = getCategoriesUseCase.execute().toList()

        assert(categoryDao.getCategories().isNotEmpty())
        assertLoadingEmissionShown(flowItems)
        assertFlowDataIsListOfAccounts(flowItems)
        assertCategoriesMatch(flowItems)
        assertLoadingEmissionNotShown(flowItems)
    }

    private fun addATestCategoriesToDb() {
        val categories = mutableListOf<CategoryEntity>()
        categories.add(CategoryEntity(id = 1, name = "Tax", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 2, name = "Grocery", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 3, name = "Entertainment", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 4, name = "Gym", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 5, name = "Health", transactionType = "Expense"))
        categories.add(CategoryEntity(id = 6, name = "Salary", transactionType = "Income"))
        categories.add(CategoryEntity(id = 7, name = "Dividends", transactionType = "Income"))

        db.categories.addAll(categories)
    }

    private fun assertFlowDataIsListOfAccounts(flowItems: List<DataState<List<TransactionCategory>>>) {
        val categories = flowItems[1].data
        assert(categories?.size ?: 0 > 0)
    }

    private fun assertFlowDataIsEmptyListOfAccounts(flowItems: List<DataState<List<TransactionCategory>>>) {
        val categories = flowItems[1].data
        MatcherAssert.assertThat(categories?.size, CoreMatchers.equalTo(0))
    }

    private fun assertCategoriesMatch(flowItems: List<DataState<List<TransactionCategory>>>) {
        val categories = flowItems[1].data
        MatcherAssert.assertThat(categories?.get(index = 0)?.name, CoreMatchers.equalTo("Tax"))
        MatcherAssert.assertThat(categories?.get(index = 1)?.name, CoreMatchers.equalTo("Grocery"))
        MatcherAssert.assertThat(categories?.get(index = 2)?.name, CoreMatchers.equalTo("Entertainment"))
        MatcherAssert.assertThat(categories?.get(index = 3)?.name, CoreMatchers.equalTo("Gym"))
        MatcherAssert.assertThat(categories?.get(index = 4)?.name, CoreMatchers.equalTo("Health"))
        MatcherAssert.assertThat(categories?.get(index = 5)?.name, CoreMatchers.equalTo("Salary"))
        MatcherAssert.assertThat(categories?.get(index = 6)?.name, CoreMatchers.equalTo("Dividends"))

        MatcherAssert.assertThat(categories?.get(index = 0)?.transactionType, CoreMatchers.equalTo("Expense"))
        MatcherAssert.assertThat(categories?.get(index = 1)?.transactionType, CoreMatchers.equalTo("Expense"))
        MatcherAssert.assertThat(categories?.get(index = 2)?.transactionType, CoreMatchers.equalTo("Expense"))
        MatcherAssert.assertThat(categories?.get(index = 3)?.transactionType, CoreMatchers.equalTo("Expense"))
        MatcherAssert.assertThat(categories?.get(index = 4)?.transactionType, CoreMatchers.equalTo("Expense"))
        MatcherAssert.assertThat(categories?.get(index = 5)?.transactionType, CoreMatchers.equalTo("Income"))
        MatcherAssert.assertThat(categories?.get(index = 6)?.transactionType, CoreMatchers.equalTo("Income"))
    }

    private fun assertLoadingEmissionShown(flowItems: List<DataState<List<TransactionCategory>>>) {
        assert(flowItems[0].loading)
    }

    private fun assertLoadingEmissionNotShown(flowItems: List<DataState<List<TransactionCategory>>>) {
        assert(!flowItems[1].loading)
    }
}