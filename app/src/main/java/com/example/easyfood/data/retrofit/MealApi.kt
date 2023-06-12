package com.example.easyfood.data.retrofit

import com.example.easyfood.data.pojo.CategoryList
import com.example.easyfood.data.pojo.PopularItemsList
import com.example.easyfood.data.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php?")
    fun getDetails(@Query ("i") id:String):Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query ("c") categoryName:String) : Call<PopularItemsList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query ("c") categoryName: String
    ) : Call<PopularItemsList>

    @GET("search.php")
    fun searchMeals(@Query("s")searchQuery: String):Call<MealList>
}