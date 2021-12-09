package com.example.recipesapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reciepsapp.R
import com.example.recipesapp.view.fragments.FavouriteRecipesFragment
import com.example.recipesapp.view.fragments.RecipesListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class RecipesContainerActivity() : AppCompatActivity() {

    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_list_activity_layout)
        initBottomNavigation()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RecipesListFragment())
            .commit()
    }

    private fun initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, RecipesListFragment())
                        .commit()
                }
                R.id.favourite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FavouriteRecipesFragment())
                        .commit()
                }
            }

            return@setOnItemSelectedListener true
        }

        bottomNavigationView?.selectedItemId = R.id.home;
    }

}