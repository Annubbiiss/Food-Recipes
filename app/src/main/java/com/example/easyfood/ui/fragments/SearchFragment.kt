package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

private lateinit var binding: FragmentSearchBinding
private lateinit var viewModel: HomeViewModel
private lateinit var mealsAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       prepareRecyclerView()
       observeSearchMealsLivedata()
        onSearchTextChange()
       onSearchClick()
        onsearchedItemClick()
    }

    private fun onSearchTextChange() {
        var searchJop : Job? = null
        binding.editTxtSearch.addTextChangedListener { searchQuery->
            searchJop?.cancel()
            searchJop = lifecycleScope.launch {
                delay(600)
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun observeSearchMealsLivedata() {
        viewModel.observeSearchMealsLivedata().observe(viewLifecycleOwner, Observer {
            mealList->
            mealsAdapter.differ.submitList(mealList)
        })
    }

    private fun onSearchClick() {
        binding.imgSearch.setOnClickListener {
            searchMeals()
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.editTxtSearch.text.toString()
        viewModel.searchMeals(searchQuery)
    }

    private fun prepareRecyclerView() {
        mealsAdapter = MealsAdapter()
        binding.searchRv.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = mealsAdapter
        }
    }

    private fun onsearchedItemClick() {
        mealsAdapter.onItemClick = {
                meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)


        }
    }


}