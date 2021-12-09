package com.example.recipesapp.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reciepsapp.R
import com.example.recipesapp.models.Recipe

class RecipeDetailsActivity : AppCompatActivity() {

    companion object{
        const val CURRENT_RECIPE = "CURRENT_RECIPE"

        fun getInstance(context: Context, selectedRecipe: Recipe){
            val intent = Intent(context, RecipeDetailsActivity::class.java)
            intent.putExtra(CURRENT_RECIPE, selectedRecipe)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        // init Views
        val recipeImage = findViewById<ImageView>(R.id.recipeImage)
        val recipeName = findViewById<TextView>(R.id.recipeName)
        val recipePublisher = findViewById<TextView>(R.id.recipePublisher)
        val recipeGradients = findViewById<TextView>(R.id.recipeGradients)

        val recipeDetails = intent.getSerializableExtra(CURRENT_RECIPE) as Recipe
        var ingredients = ""

        Glide.with(this).load(recipeDetails.featuredImage).into(recipeImage)
        recipeName.text = recipeDetails.title
        recipePublisher.text = recipeDetails.publisher

        for(ingredient in recipeDetails.ingredients){
            ingredients += ingredient + "\n"
        }

        recipeGradients.text = ingredients

    }

}