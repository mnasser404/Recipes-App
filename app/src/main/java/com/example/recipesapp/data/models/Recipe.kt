package com.example.recipesapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipeId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("featured_image") val featuredImage: String,
    @SerializedName("ingredients") val ingredients: ArrayList<String>,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("title") val title: String,
    var isFavourite : Boolean
) : Serializable