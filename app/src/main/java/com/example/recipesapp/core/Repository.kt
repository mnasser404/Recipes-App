package com.example.recipesapp.core

import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse

interface Repository {

    suspend fun getRecipesFromRemote(page: Int) : Resource<RecipesResponse>

    suspend fun searchForRecipe(recipe : String) : Resource<RecipesResponse>

    suspend fun getRecipesFromCache() : List<Recipe>

    suspend fun insertIntoDatabase(recipe: Recipe)

    suspend fun removeFromDatabase(recipe: Recipe)

}