package com.example.recipesapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.recipesapp.models.Recipe

@Dao
interface DAO {

    @Insert
    fun insert(recipe : Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

}