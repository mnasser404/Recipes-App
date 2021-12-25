package com.example.recipesapp.data.repository

import com.example.recipesapp.utils.Resource
import com.example.recipesapp.data.models.Recipe
import com.example.recipesapp.data.models.RecipesResponse

interface Repository {

    suspend fun getRecipesFromRemote(page: Int) : Resource<RecipesResponse>

    suspend fun searchForRecipe(recipe : String) : Resource<RecipesResponse>

    suspend fun getRecipesFromCache() : List<Recipe>

    suspend fun insertIntoDatabase(recipe: Recipe)

    suspend fun removeFromDatabase(recipe: Recipe)

}