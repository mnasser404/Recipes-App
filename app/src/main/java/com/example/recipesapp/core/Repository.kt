package com.example.recipesapp.core

import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse
import retrofit2.Response

interface Repository {

    suspend fun getRecipesFromRemote(page: Int) : Response<RecipesResponse>

    suspend fun searchForRecipe(recipe : String) : Response<RecipesResponse>

    suspend fun getRecipesFromCache() : List<Recipe>

    suspend fun insertIntoDatabase(recipe: Recipe)

    suspend fun removeFromDatabase(recipe: Recipe)

}