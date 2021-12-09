package com.example.recipesapp.core

import com.example.recipesapp.api.RecipeApi
import com.example.recipesapp.db.AppDatabase
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse
import retrofit2.Response

class RepositoryImp(private val recipeApi: RecipeApi, private val appDatabase: AppDatabase) : Repository {

    override suspend fun getRecipesFromRemote(page: Int) : Response<RecipesResponse> {
       return recipeApi.getRecipes(
            page = page,
            query = "",
            token = Constants.API_KEY
        )
    }

    override suspend fun searchForRecipe(recipe: String) : Response<RecipesResponse> {
        return recipeApi.searchForRecipe(
            query = recipe,
            token = Constants.API_KEY
        )
    }

    override suspend fun getRecipesFromCache() : List<Recipe> { return appDatabase.recipeDao().getAll() }

    override suspend fun insertIntoDatabase(recipe: Recipe) = appDatabase.recipeDao().insert(recipe)

    override suspend fun removeFromDatabase(recipe: Recipe) = appDatabase.recipeDao().delete(recipe)

}