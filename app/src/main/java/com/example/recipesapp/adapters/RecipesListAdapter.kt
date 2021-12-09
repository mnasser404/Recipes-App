package com.example.recipesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reciepsapp.R
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.view.activities.RecipeDetailsActivity
import com.example.recipesapp.viewmodel.RecipeListViewModel
import com.like.LikeButton
import com.like.OnLikeListener

class RecipesListAdapter(var viewModel: RecipeListViewModel) : RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder>() {

    private var recipesItems = ArrayList<Recipe>()

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        var recipeName: TextView = itemView.findViewById(R.id.recipeName)
        var recipePublisher: TextView = itemView.findViewById(R.id.recipePublisher)
        var favoriteBtn : LikeButton = itemView.findViewById(R.id.star_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemViewHolder = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_view_layout, parent, false)
        return RecipeViewHolder(itemViewHolder)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = recipesItems[position]

        holder.recipeName.text = currentItem.title
        holder.recipePublisher.text = currentItem.publisher
        holder.favoriteBtn.isLiked = currentItem.isFavourite

        Glide.with(holder.itemView.context)
            .load(currentItem.featuredImage)
            .into(holder.recipeImage)

        holder.favoriteBtn.setOnLikeListener(object : OnLikeListener{
            override fun liked(likeButton: LikeButton?) {
                currentItem.isFavourite = true
                viewModel.saveRecipeToDatabase(currentItem)
            }

            override fun unLiked(likeButton: LikeButton?) {
                currentItem.isFavourite = false
                viewModel.deleteRecipeFromDatabase(currentItem)
                recipesItems.remove(currentItem)
                notifyDataSetChanged()
            }
        })

        holder.itemView.setOnClickListener {
            RecipeDetailsActivity.getInstance(holder.itemView.context, currentItem)
        }
    }

    override fun getItemCount(): Int {
        return recipesItems.size
    }

    fun updateAdapterList(recipesItems : ArrayList<Recipe>){
        this.recipesItems.addAll(recipesItems)
        notifyDataSetChanged()
    }

}