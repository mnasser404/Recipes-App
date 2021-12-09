package com.example.recipesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.EspressoIdleResource
import com.example.recipesapp.core.Repository
import com.example.recipesapp.core.Resource
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecipeListViewModel(private val repository: Repository) : ViewModel() {

    private var recipesMutableLiveData = MutableLiveData<Resource<RecipesResponse>>()
    private var recipesCachedMutableLiveData = MutableLiveData<Resource<List<Recipe>>>()
    private var searchedRecipesMutableLiveData = MutableLiveData<Resource<List<Recipe>>>()
    private var pageNumber = 1
    private var totalItemsCount = 0

    fun nextPage() {
        pageNumber++;
    }

    fun getRecipes() {
        EspressoIdleResource.increment()
        viewModelScope.async {
            val result = viewModelScope.async {
                    recipesMutableLiveData.postValue(Resource.Loading())
                    val response = repository.getRecipesFromRemote(pageNumber)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            updateRemoteWithCachedItems(it.results)
                            recipesMutableLiveData.postValue(Resource.Success(it))
                            totalItemsCount = it.count
                        }
                    } else {
                        recipesMutableLiveData.postValue(Resource.Error(response.message()))
                    }
                EspressoIdleResource.decrement()
            }
            result.await()
        }
    }

    private fun updateRemoteWithCachedItems(remoteItems : List<Recipe>) : List<Recipe> {
            viewModelScope.async {
                val result = viewModelScope.async {
                    repository.getRecipesFromCache()
                }
                val recipesList = result.await()
                recipesList?.let {
                    for(currentCachedRecipe in it){
                        for(currentRemoteRecipe in remoteItems){
                            if(currentCachedRecipe.title == currentRemoteRecipe.title){
                                currentRemoteRecipe.isFavourite = true
                                break
                            }
                        }
                    }
                }
            }
        return remoteItems
    }


    fun getCachedRecipes() {
        viewModelScope.async {
            val result = viewModelScope.async {
                repository.getRecipesFromCache()
            }
            val recipesList = result.await()
            recipesList?.let {
                recipesCachedMutableLiveData.postValue(Resource.Success(recipesList))
            }
        }
    }

    fun searchForRecipe(recipeName: String) {
        viewModelScope.async {
            val result = viewModelScope.async {
                recipesMutableLiveData.postValue(Resource.Loading())
                val response = repository.searchForRecipe(recipeName)
                if (response.isSuccessful) {
                    response.body()?.let {
                        recipesMutableLiveData.postValue(Resource.Success(it))
                        totalItemsCount = it.count
                    }
                } else {
                    recipesMutableLiveData.postValue(Resource.Error(response.message()))
                }
            }
            result.await()
        }
    }

    fun saveRecipeToDatabase(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertIntoDatabase(recipe)
        }
    }

    fun deleteRecipeFromDatabase(recipe: Recipe) {
        viewModelScope.launch {
            repository.removeFromDatabase(recipe)
        }
    }

    fun getRecipesLiveData(): LiveData<Resource<RecipesResponse>> {
        return recipesMutableLiveData
    }

    fun getCachedRecipesLiveData(): LiveData<Resource<List<Recipe>>> {
        return recipesCachedMutableLiveData
    }

    fun getTotalItemsCount(): Int {
        return totalItemsCount;
    }

}