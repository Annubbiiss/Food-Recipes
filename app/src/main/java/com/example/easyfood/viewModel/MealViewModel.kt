package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.db.MealDb
import com.example.easyfood.data.pojo.Meal
import com.example.easyfood.data.pojo.MealList
import com.example.easyfood.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDb: MealDb):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id : String){
        RetrofitInstance.api.getDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
response.body()?.let {

    mealDetailsLiveData.postValue(response.body()!!.meals[0])

}

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {

                Log.d("MealActivity",t.message.toString())
            }
        })

    }
     fun observeMealDetailLivedata(): LiveData<Meal?> {
        return mealDetailsLiveData
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDb.mealDao().upsert(meal)
        }

    }

}