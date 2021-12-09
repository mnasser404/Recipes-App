package com.example.recipesapp.models

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") var results: List<Recipe>
)
