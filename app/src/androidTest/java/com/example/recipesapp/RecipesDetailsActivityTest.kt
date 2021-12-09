package com.example.recipesapp

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.reciepsapp.R
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.view.activities.RecipeDetailsActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RecipesDetailsActivityTest {

    @get:Rule
    val activityRule  = ActivityTestRule(RecipeDetailsActivity::class.java, false,false)
    private var recipe : Recipe? = null

    @Before
    fun registerIdleResource() {
        recipe = Recipe(1, "cake description1", "", ArrayList(), "publisher1", "Cauldron&nbsp;Cakes - Home - Pastry Affair", false)
        val intent = Intent()
        intent.putExtra(RecipeDetailsActivity.CURRENT_RECIPE, recipe)
        activityRule.launchActivity(intent)
    }

    @Test
    fun is_details_recipes_activity_is_visible() {
        Espresso.onView(ViewMatchers.withId(R.id.activityRecipeDetails))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun is_recipe_name_and_publisher_set_correct_from_bundle(){
        Espresso.onView(ViewMatchers.withId(R.id.recipeName)).check(ViewAssertions.matches(ViewMatchers.withText(recipe?.title)))
        Espresso.onView(ViewMatchers.withId(R.id.recipePublisher)).check(ViewAssertions.matches(ViewMatchers.withText(recipe?.publisher)))
    }

}