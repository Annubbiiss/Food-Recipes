package com.example.easyfood.data.pojo


import com.google.gson.annotations.SerializedName

data class PopularItemsList(
    @SerializedName("meals")
    val meals: List<PopularItems>? = listOf()
)