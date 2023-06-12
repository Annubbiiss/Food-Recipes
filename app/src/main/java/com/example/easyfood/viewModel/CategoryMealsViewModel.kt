package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.pojo.PopularItems
import com.example.easyfood.data.pojo.PopularItemsList
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    val mealsLivedata = MutableLiveData<List<PopularItems>>()
    fun getMealsByCategory(categoryName : String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<PopularItemsList>{
            override fun onResponse(
                call: Call<PopularItemsList>,
                response: Response<PopularItemsList>
            ) {
                if (response.body()!= null){
                    mealsLivedata.value = response.body()!!.meals!!

                }
            }

            override fun onFailure(call: Call<PopularItemsList>, t: Throwable) {
                Log.d("dv",t.message.toString())
            }


        })

    }
    fun observeMealsCategory(): MutableLiveData<List<PopularItems>> {
        return mealsLivedata
    }
}