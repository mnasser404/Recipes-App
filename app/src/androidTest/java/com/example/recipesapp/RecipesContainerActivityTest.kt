package com.example.recipesapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.reciepsapp.R
import com.example.recipesapp.presentation.RecipesContainerActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RecipesContainerActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RecipesContainerActivity::class.java)

    @Test
    fun is_bottomNavigationBar_isVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.bottomNavigationView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun is_home_button_in_bottomNavigationBar_navigate_to_recipesList(){
        Espresso.onView(ViewMatchers.withId(R.id.home)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.recipesListFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun is_favourite_button_in_bottomNavigationBar_navigate_to_favouriteList(){
        Espresso.onView(ViewMatchers.withId(R.id.favourite)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.favouriteRecipesList)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}