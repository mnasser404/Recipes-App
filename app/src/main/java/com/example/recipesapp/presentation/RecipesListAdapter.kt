package com.example.recipesapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reciepsapp.R
import com.example.recipesapp.data.models.Recipe
import com.like.LikeButton
import com.like.OnLikeListener

class RecipesListAdapter(var isFavouriteScreen : Boolean,
                         var itemClickedCallBack : (selectedItem : Recipe) -> Unit,
                         var favouriteClickedCallBack : (isFavourite : Boolean, selectedItem : Recipe) -> Unit) : RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder>() {

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
                favouriteClickedCallBack(true, currentItem)
            }
            override fun unLiked(likeButton: LikeButton?) {
                currentItem.isFavourite = false
                if(isFavouriteScreen){
                    recipesItems.remove(currentItem)
                    notifyDataSetChanged()
                }
                favouriteClickedCallBack(false, currentItem)
            }
        })

        holder.itemView.setOnClickListener {
            itemClickedCallBack(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return recipesItems.size
    }

    fun updateAdaperList(recipesItems : ArrayList<Recipe>){
        this.recipesItems.addAll(recipesItems)
        notifyDataSetChanged()
    }

}