package com.example.easyfood.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easyfood.data.db.MealDb

class HomeViewModelFactory(private val mealDb: MealDb): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDb) as T
    }
}