package com.example.recipesapp.data.models

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("results") var results: List<Recipe>
)
