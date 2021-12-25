package com.example.recipesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipesapp.data.models.Recipe
import com.example.recipesapp.data.models.RecipesResponse
import com.example.recipesapp.data.repository.Repository
import com.example.recipesapp.presentation.RecipeListViewModel
import com.example.recipesapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecipesListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var successResponse : Resource.Success<RecipesResponse>? = null
    private var failureResponse : Resource.Error<RecipesResponse>? = null
    private var cachedRecipesList : MutableList<Recipe>? = null
    private var recipesResponse : RecipesResponse? = null
    private var recipesList : MutableList<Recipe>? = null
    private var viewModel : RecipeListViewModel? = null
    private var repository : Repository? = null


    @Before
    fun setup(){
        repository = Mockito.mock(Repository::class.java)
        viewModel = RecipeListViewModel(repository!!)
        initializeRecipesList()
        initializeCachedRecipesList()
        recipesResponse = RecipesResponse(3, recipesList!!)
        successResponse = Resource.Success(recipesResponse!!)
        failureResponse = Resource.Error("UnAuthorized", 400, null)
    }

    private fun initializeRecipesList() {
        val recipe1 = Recipe(1, "recipe Description1", "", arrayListOf(), "publisher 1", "title 1", false)
        val recipe2 = Recipe(2, "recipe Description2", "", arrayListOf(), "publisher 2", "title 2", true)
        val recipe3 = Recipe(3, "recipe Description3", "", arrayListOf(), "publisher 3", "title 3", false)
        recipesList = mutableListOf()
        recipesList!!.add(recipe1)
        recipesList!!.add(recipe2)
        recipesList!!.add(recipe3)
    }

    private fun initializeCachedRecipesList() {
        val recipe1 = Recipe(1, "recipe Description1", "", arrayListOf(), "publisher 1", "title 1", true)
        val recipe2 = Recipe(2, "recipe Description2", "", arrayListOf(), "publisher 2", "title 2", true)
        val recipe3 = Recipe(3, "recipe Description3", "", arrayListOf(), "publisher 3", "title 3", true)
        cachedRecipesList = mutableListOf()
        cachedRecipesList!!.add(recipe1)
        cachedRecipesList!!.add(recipe2)
        cachedRecipesList!!.add(recipe3)
    }

    @Test
    fun test_success_in_get_recipes_function(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            viewModel!!.getRecipes()
            assertEquals(successResponse?.data?.results, viewModel!!.getRecipesLiveData()?.value?.data?.results)
        }
    }

    @Test
    fun test_failure_in_get_recipes_function(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(failureResponse)
            viewModel!!.getRecipes()
            assertEquals(failureResponse?.message, viewModel!!.getRecipesLiveData()?.value?.message)
            assertEquals(failureResponse?.errorCode, viewModel!!.getRecipesLiveData()?.value?.errorCode)
        }
    }

    @Test
    fun test_isFavourite_is_true_after_saving_recipe_in_database(){
        testCoroutineRule.runBlockingTest {
            val recipe = recipesList!![0]
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            viewModel!!.getRecipes()
            viewModel!!.saveRecipeToDatabase(recipe)
            assertTrue { viewModel!!.getRecipesLiveData().value?.data?.results?.get(0)?.isFavourite == true }
        }
    }

    @Test
    fun test_isFavourite_is_false_after_removing_recipe_in_database(){
        testCoroutineRule.runBlockingTest {
            val recipe = recipesList!![1]
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            viewModel!!.getRecipes()
            viewModel!!.removeRecipeFromDatabase(recipe)
            assertTrue { viewModel!!.getRecipesLiveData().value?.data?.results?.get(1)?.isFavourite == false }
        }
    }

    @Test
    fun test_total_item_count_that_retrieved_correct_from_the_api(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            viewModel!!.getRecipes()
            assertEquals(successResponse?.data?.count, viewModel!!.getTotalItemsCount())
        }
    }

    @Test
    fun increase_page_number_is_correct_when_user_scroll_to_bottom(){
        viewModel!!.nextPage()
        assertEquals(2, viewModel!!.getPageNumber())
    }

    @Test
    fun reset_page_number_returns_to_it_is_default_value(){
        viewModel!!.nextPage()
        viewModel!!.resetPageNumber()
        assertEquals(1, viewModel!!.getPageNumber())
    }

    @Test
    fun is_remote_favourite_items_being_updated_with_cached_values_after_retrieving_from_api(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository!!.getRecipesFromRemote(1)).thenReturn(successResponse)
            Mockito.`when`(repository!!.getRecipesFromCache()).thenReturn(cachedRecipesList)
            viewModel!!.getRecipes()
            assertTrue { viewModel!!.getRecipesLiveData().value?.data?.results?.get(0)?.isFavourite == true }
            assertTrue { viewModel!!.getRecipesLiveData().value?.data?.results?.get(2)?.isFavourite == true }
        }
    }

}