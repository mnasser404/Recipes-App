package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.api.RecipeApi
import com.example.recipesapp.core.Constants
import com.example.recipesapp.core.Repository
import com.example.recipesapp.core.RepositoryImp
import com.example.recipesapp.db.AppDatabase
import com.example.recipesapp.viewmodel.RecipeListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { getAppDataBase(androidContext()) }
    single { getRecipeApiInstance() }
    single { getRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel {
        RecipeListViewModel(get())
    }
}


fun getRepository(recipeApi: RecipeApi, appDatabase: AppDatabase) : Repository{
    return RepositoryImp(recipeApi, appDatabase)
}

fun getAppDataBase(context : Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "recipes").allowMainThreadQueries().build()
}

fun getRecipeApiInstance() : RecipeApi {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApi::class.java)
}