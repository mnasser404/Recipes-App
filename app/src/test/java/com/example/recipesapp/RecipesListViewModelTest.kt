package com.example.recipesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipesapp.core.Repository
import com.example.recipesapp.core.Resource
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse
import com.example.recipesapp.viewmodel.RecipeListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecipesListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var successResponse : Resource.Success<RecipesResponse>? = null
    private var failureResponse : Resource.Error<RecipesResponse>? = null
    private var recipesResponse : RecipesResponse? = null
    private var recipesList : MutableList<Recipe>? = null
    private var viewModel : RecipeListViewModel? = null
    private var repository : Repository? = null


    @Before
    fun setup(){
        repository = Mockito.mock(Repository::class.java)
        viewModel = RecipeListViewModel(repository!!)
        initializeRecipesList()
        recipesResponse = RecipesResponse(3, recipesList!!)
        successResponse = Resource.Success(recipesResponse!!)
        failureResponse = Resource.Error("UnAuthorized", 400, null)
    }

    private fun initializeRecipesList() {
        val recipe1 = Recipe(1, "recipe Description1", "", arrayListOf(), "publisher 1", "title 1", false)
        val recipe2 = Recipe(2, "recipe Description2", "", arrayListOf(), "publisher 2", "title 2", false)
        val recipe3 = Recipe(3, "recipe Description3", "", arrayListOf(), "publisher 3", "title 3", false)
        recipesList = mutableListOf()
        recipesList!!.add(recipe1)
        recipesList!!.add(recipe2)
        recipesList!!.add(recipe3)
    }

    @Test
    fun test_success_in_get_recipes_function(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            viewModel!!.getRecipes()
            assertEquals(viewModel!!.getRecipesLiveData().value?.data?.results, successResponse?.data?.results)
        }
    }

    @Test
    fun test_failure_in_get_recipes_function(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(failureResponse)
            viewModel!!.getRecipes()
            assertEquals(viewModel!!.getRecipesLiveData().value?.message, failureResponse?.message)
            assertEquals(viewModel!!.getRecipesLiveData().value?.errorCode, failureResponse?.errorCode)
        }
    }

    @Test
    fun test_saving_recipes_in_database(){
        testCoroutineRule.runBlockingTest {
            val recipe = recipesList!![0]
            viewModel!!.saveRecipeToDatabase(recipe)
            viewModel!!.getCachedRecipes()
            assertNotNull(viewModel!!.getCachedRecipesLiveData().value)
        }
    }


}