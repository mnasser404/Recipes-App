package com.example.recipesapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.Repository
import com.example.recipesapp.utils.Resource
import com.example.recipesapp.data.models.Recipe
import com.example.recipesapp.data.models.RecipesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecipeListViewModel(val repository: Repository) : ViewModel() {

    private var recipesMutableLiveData : MutableLiveData<Resource<RecipesResponse>> = MutableLiveData<Resource<RecipesResponse>>()
    private var recipesCachedMutableLiveData : MutableLiveData<Resource<List<Recipe>>> = MutableLiveData<Resource<List<Recipe>>>()
    private var pageNumber = 1
    private var totalItemsCount = 0

    fun nextPage() {
        pageNumber++;
    }

    fun resetPageNumber() {
        this.pageNumber = 1
    }

    fun getRecipes() {
        //EspressoIdleResource.increment()
        viewModelScope.async {
            val result = viewModelScope.async {
                recipesMutableLiveData.postValue(Resource.Loading())
                    val response = repository.getRecipesFromRemote(getPageNumber())
                        response.data?.let {
                            updateRemoteWithCachedItems(it.results)
                            totalItemsCount = it.count
                        }
                recipesMutableLiveData.postValue(response)
                //EspressoIdleResource.decrement()
            }
            result.await()
        }
    }

    fun updateRemoteWithCachedItems(remoteItems : List<Recipe>?) : List<Recipe>? {
        if(!remoteItems.isNullOrEmpty()){
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

    fun saveRecipeToDatabase(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertIntoDatabase(recipe)
            val recipe = getRecipesLiveData().value?.data?.results?.find { it.title == recipe.title }
            recipe?.isFavourite = true
        }
    }

    fun removeRecipeFromDatabase(recipe: Recipe) {
        viewModelScope.launch {
            repository.removeFromDatabase(recipe)
            val recipe = getRecipesLiveData().value?.data?.results?.find { it.title == recipe.title }
            recipe?.isFavourite = false
        }
    }

    fun getRecipesLiveData(): LiveData<Resource<RecipesResponse>> {
        return recipesMutableLiveData
    }

    fun getCachedRecipesLiveData(): LiveData<Resource<List<Recipe>>>? {
        return recipesCachedMutableLiveData
    }

    fun getTotalItemsCount(): Int {
        return totalItemsCount
    }

    fun getPageNumber(): Int{
        return pageNumber
    }

}