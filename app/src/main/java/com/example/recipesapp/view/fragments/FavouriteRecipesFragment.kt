package com.example.recipesapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciepsapp.R
import com.example.recipesapp.adapters.RecipesListAdapter
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.viewmodel.RecipeListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FavouriteRecipesFragment : Fragment() {

    private lateinit var recipesListAdapter: RecipesListAdapter
    private val viewModel : RecipeListViewModel by sharedViewModel()


    private fun initializeRecyclerView(rootView: View) {
        val recipesRecyclerView = rootView.findViewById<RecyclerView>(R.id.recipesFavouritesRecyclerView)
        recipesListAdapter = RecipesListAdapter(viewModel)
        recipesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recipesRecyclerView.adapter = recipesListAdapter
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_favourite_recipes, container, false)
        initializeRecyclerView(view)
        viewModel.getCachedRecipes()
        viewModel.getCachedRecipesLiveData().observe(viewLifecycleOwner, {
            recipesListAdapter.updateAdapterList(it.data!! as ArrayList<Recipe>)
        })
        return view
    }

}