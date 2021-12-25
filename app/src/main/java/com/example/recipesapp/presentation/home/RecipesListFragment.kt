package com.example.recipesapp.presentation.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciepsapp.R
import com.example.recipesapp.data.models.Recipe
import com.example.recipesapp.presentation.RecipeListViewModel
import com.example.recipesapp.presentation.RecipesListAdapter
import com.example.recipesapp.presentation.base.BaseFragment
import com.example.recipesapp.presentation.details.RecipeDetailsActivity
import com.example.recipesapp.utils.Resource
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RecipesListFragment : BaseFragment() {

    private val viewModel: RecipeListViewModel by sharedViewModel()
    private lateinit var recipesListAdapter: RecipesListAdapter

    override fun loadLayoutFromResIdToViewStub(rootView: View?, container: ViewGroup?) {
        val stub: ViewStub = rootView?.findViewById(R.id.child_fragment_content_holder)!!
        stub.layoutResource = R.layout.fragment_recipes_list
        stub.inflate()
        initializeRecyclerView(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.resetPageNumber()
        viewModel.getRecipes()
        viewModel.getRecipesLiveData().observe(this ){
            when (it) {
                is Resource.Success -> {
                    showHideProgressBar(false)
                    recipesListAdapter.updateAdaperList(it.data?.results as ArrayList<Recipe>)
                }
                is Resource.Error -> {
                    showHideProgressBar(false)
                    Toast.makeText(requireActivity(), it.message + " / " + it.errorCode, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showHideProgressBar(true)
                }
            }
        }
    }


    private fun initializeRecyclerView(rootView: View) {
        val recipesRecyclerView = rootView.findViewById<RecyclerView>(R.id.recipesRecyclerView)
        recipesListAdapter = RecipesListAdapter(false,
            { RecipeDetailsActivity.getInstance(requireActivity(), it) },
            { isFavourite, selectedItem ->
                if(isFavourite) {
                    viewModel.saveRecipeToDatabase(selectedItem)
                }else {
                    viewModel.removeRecipeFromDatabase(selectedItem)
                }
            }
        )
        recipesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recipesRecyclerView.adapter = recipesListAdapter
        recipesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recipesRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == recipesListAdapter.itemCount - 1
                    && recipesListAdapter.itemCount < viewModel.getTotalItemsCount()
                ) {
                    viewModel.nextPage()
                    viewModel.getRecipes()
                }
            }
        })
    }


}