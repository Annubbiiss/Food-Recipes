package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.db.MealDb
import com.example.easyfood.data.pojo.*
import com.example.easyfood.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDb: MealDb) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal?>()
    private var popularItemsLiveData = MutableLiveData<List<PopularItems>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()
    private var favoritesMealsLiveData = mealDb.mealDao().getAllMeals()
    private var mealBottomSheetLiveData = MutableLiveData<Meal>()

    init {
        getRandomMeal()
    }

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {

                if (response.body() != null){
                    val randomMeal : Meal? = response.body()!!.meals[0]
                   randomMealLiveData.value = randomMeal
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home",t.message.toString())            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<PopularItemsList>{
            override fun onResponse(call: Call<PopularItemsList>, response: Response<PopularItemsList>) {
                if (response.body()!= null){
                    popularItemsLiveData.value = response.body()!!.meals!!

                }

            }

            override fun onFailure(call: Call<PopularItemsList>, t: Throwable) {
Log.d("dv",t.message.toString())
            }


        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body()!= null){
                    categoriesLiveData.value = (response.body()!!.categories as List<Category>?)

                }

            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Home",t.message.toString())            }


        })

    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            response.body()?.let {
                searchMealsLiveData.value = response.body()?.meals as List<Meal>?
            }
        }
        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.d("dv",t.message.toString())
        }


    })

    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDb.mealDao().delete(meal)
        }

    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDb.mealDao().upsert(meal)
        }

    }

     fun observeRandomMealLivedata(): LiveData<Meal?> {
        return randomMealLiveData
    }

    fun observPopularItemsLivedata():LiveData<List<PopularItems>>{
        return popularItemsLiveData

    }

    fun observeCategoriesLivedata():LiveData<List<Category>>{
        return categoriesLiveData

    }

    fun obseveFavotitesMealsLivedata():LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal->
                    mealBottomSheetLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("dv", t.message.toString())
            }


        })

    }

    fun observeBottomSheetLivedata():LiveData<Meal> {
        return mealBottomSheetLiveData
    }

    fun observeSearchMealsLivedata() :LiveData<List<Meal>> {
        return searchMealsLiveData
    }

}