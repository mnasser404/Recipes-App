package com.example.recipesapp.core

import com.example.recipesapp.api.RecipeApi
import com.example.recipesapp.db.AppDatabase
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.RecipesResponse
import retrofit2.HttpException

class RepositoryImp(private val recipeApi: RecipeApi, private val appDatabase: AppDatabase) : Repository {

    override suspend fun getRecipesFromRemote(page: Int) : Resource<RecipesResponse> {
        return try{
            val response =  recipeApi.getRecipes(
                page = page,
                query = "",
                token = Constants.API_KEY
            )
            Resource.Success(response)
        }catch (ex : HttpException){
            Resource.Error(ex.message(), ex.code(), null)
        }
    }

    override suspend fun searchForRecipe(recipe: String) : Resource<RecipesResponse> {
        return try{
            val response = recipeApi.searchForRecipe(
                query = recipe,
                token = Constants.API_KEY
            )
            Resource.Success(response)
        }catch (ex : HttpException){
            Resource.Error(ex.message(), ex.code(), null)
        }
    }

    override suspend fun getRecipesFromCache() : List<Recipe> { return appDatabase.recipeDao().getAll() }

    override suspend fun insertIntoDatabase(recipe: Recipe) = appDatabase.recipeDao().insert(recipe)

    override suspend fun removeFromDatabase(recipe: Recipe) = appDatabase.recipeDao().delete(recipe)

}