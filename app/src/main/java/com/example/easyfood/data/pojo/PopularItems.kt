package com.example.easyfood.data.pojo


import com.google.gson.annotations.SerializedName

data class PopularItems(
    @SerializedName("idMeal")
    val idMeal: String? = null,
    @SerializedName("strMeal")
    val strMeal: String? = null,
    @SerializedName("strMealThumb")
    val strMealThumb: String? = null
)