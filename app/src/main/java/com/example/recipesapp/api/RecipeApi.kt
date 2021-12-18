package com.example.recipesapp.api

import com.example.recipesapp.core.Constants.Companion.GET_RECIPES
import com.example.recipesapp.models.RecipesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApi {

    @GET(GET_RECIPES)
    suspend fun getRecipes(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Header("Authorization") token: String
    ): RecipesResponse


    @GET(GET_RECIPES)
    suspend fun searchForRecipe(
        @Query("query") query: String,
        @Header("Authorization") token: String
    ): RecipesResponse

}
