package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.ui.activities.CategoryMealsActivity
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.data.pojo.Category
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    companion object{
        const val CATEGORY_NAME = "com.example.easyfood.ui.fragments.categoryName"
    }

private lateinit var binding: FragmentCategoriesBinding
private lateinit var categoriesAdapter :CategoriesAdapter
private lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCtegoriesAdapter()
        onCategoryClick()
        observeCtegories()
    }

    private fun observeCtegories() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner, {
            categories->
            categoriesAdapter.setCategories(categories as ArrayList<Category>)
        })

    }
    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
                category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)

            startActivity(intent)
        }

    }
    private fun prepareCtegoriesAdapter() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategoryFragment.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }




}