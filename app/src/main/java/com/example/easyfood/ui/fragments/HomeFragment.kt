package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.ui.activities.CategoryMealsActivity
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularAdapter
import com.example.easyfood.data.pojo.Category
import com.example.easyfood.data.pojo.Meal
import com.example.easyfood.data.pojo.PopularItems
import com.example.easyfood.ui.MealBottomSheetFragment
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.R
import com.example.easyfood.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
private lateinit var binding: FragmentHomeBinding
private lateinit var viewModel: HomeViewModel
private lateinit var randomMeal : Meal
private lateinit var popularItemsAdapter: MostPopularAdapter
private lateinit var categoriesAdapter: CategoriesAdapter
lateinit var onItemLongClick :((Meal)->Unit)
companion object{

    const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
    const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
    const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
    const val CATEGORY_NAME = "com.example.easyfood.fragments.categoryName"
}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopulaItemsRecyclerview()
        prepareCategoriesRecyclerview()


        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopulaItemsLivedata()
        onPopularItemClick()

        viewModel.getCategories()
        observeCategoriesLivedata()
        onCategoryClick()

        onPopularItemLongClick()
        onRandomMealLongClick()
        onSearchClick()

    }

    private fun onSearchClick() {
        binding.ingSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
         popularItemsAdapter.onItemLongClick = {
             popularItems ->
             val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(popularItems.idMeal.toString())
             mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
         }
    }


    private fun onCategoryClick() {
      categoriesAdapter.onItemClick = {
          category ->
          val intent = Intent(activity,CategoryMealsActivity::class.java)
          intent.putExtra(CATEGORY_NAME,category.strCategory)

          startActivity(intent)
      }

    }

    private fun prepareCategoriesRecyclerview() {
        binding.recyclerView.apply {

            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }

    }

    private fun observeCategoriesLivedata() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner ){
            categories->


                categoriesAdapter.setCategories(categoriesList = categories as ArrayList<Category>)


        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopulaItemsRecyclerview() {
        binding.recViewMealsPopular.apply {

            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopulaItemsLivedata() {
       viewModel.observPopularItemsLivedata().observe(viewLifecycleOwner
       ) {
           mealList->
           popularItemsAdapter.setMeals(mealList = mealList as ArrayList<PopularItems> )
       }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner,
            { meal ->

                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)
                this.randomMeal = meal


            })
    }
    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)

        }
    }


    private fun onRandomMealLongClick() {
        binding.imgRandomMeal.setOnLongClickListener {
            onItemLongClick.invoke(randomMeal)
            true
        }

        onItemLongClick = {

            meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")

        }

    }



}