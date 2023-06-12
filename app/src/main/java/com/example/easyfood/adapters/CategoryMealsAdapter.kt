package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealCardBinding
import com.example.easyfood.data.pojo.PopularItems

class CategoryMealsAdapter:RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealViewHolder>() {
    class CategoryMealViewHolder(val binding: MealCardBinding):RecyclerView.ViewHolder(binding.root)

    private var mealList = ArrayList<PopularItems>()

    fun setCategories(mealList: List <PopularItems>){
        this.mealList  = mealList as ArrayList<PopularItems>
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(MealCardBinding
            .inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
    .load(mealList[position].strMealThumb)
    .into(holder.binding.imgMeal)
    holder.binding.tvMealName.text = mealList[position].strMeal
    }

    override fun getItemCount(): Int {
return mealList.size   }
}