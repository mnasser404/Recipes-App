package com.example.recipesapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.reciepsapp.R
import com.example.recipesapp.presentation.RecipesListAdapter
import com.example.recipesapp.data.models.Recipe
import com.example.recipesapp.utils.EspressoIdleResource
import com.example.recipesapp.presentation.RecipesContainerActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RecipesContainerActivity::class.java)

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdleResource.countingIdleResource)
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdleResource.countingIdleResource)
    }

    @Test
    fun is_recipesList_fragment_isVisible(){
        Espresso.onView(ViewMatchers.withId(R.id.recipesListFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun is_recyclerView_isVisible(){
        Espresso.onView(ViewMatchers.withId(R.id.recipesRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun is_recipeDetails_show_when_click_on_item(){

        Espresso.onView(ViewMatchers.withId(R.id.recipesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecipesListAdapter.RecipeViewHolder>(1, ViewActions.click()))

        Espresso.onView(ViewMatchers.withId(R.id.recipeDetailsLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun is_recipeDetails_title_isCorrect_show_when_click_on_item(){

        val recipe = Recipe(1, "cake description1", "",
            ArrayList(), "publisher1", "Cauldron&nbsp;Cakes - Home - Pastry Affair", false)

        Espresso.onView(ViewMatchers.withId(R.id.recipesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecipesListAdapter.RecipeViewHolder>(0, ViewActions.click()))

        Espresso.onView(ViewMatchers.withId(R.id.recipeName)).check(ViewAssertions.matches(ViewMatchers.withText(recipe.title)))
    }

    @Test
    fun is_recipes_list_shows_again_when_pressBack_in_details_screen(){

        val recipe = Recipe(1, "cake description1", "",
            ArrayList(), "publisher1", "Cauldron&nbsp;Cakes - Home - Pastry Affair", false)

        Espresso.onView(ViewMatchers.withId(R.id.recipesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecipesListAdapter.RecipeViewHolder>(0, ViewActions.click()))

        Espresso.onView(ViewMatchers.withId(R.id.recipeName)).check(ViewAssertions.matches(ViewMatchers.withText(recipe.title)))

        Espresso.pressBack()

        Espresso.onView(ViewMatchers.withId(R.id.recipesRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}