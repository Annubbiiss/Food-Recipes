package com.example.easyfood.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.databinding.FragmentMealButtomSheetBinding
import com.example.easyfood.ui.fragments.HomeFragment
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val mealId = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMealButtomSheetBinding
    private lateinit var viewModel: HomeViewModel
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThump: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        arguments?.let {
            mealId = it.getString(mealId)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealButtomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { mealId->
            viewModel.getMealById(mealId)
        }
        observeBottomSheetLivedata()
        onSeeMoreClick()
    }

    private fun onSeeMoreClick() {
        binding.seeAllInformation.setOnClickListener {
            if (mealName != null && mealThump != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID,mealId)
                intent.putExtra(HomeFragment.MEAL_NAME,mealName)
                intent.putExtra(HomeFragment.MEAL_THUMB,mealThump)
                startActivity(intent)
            }
        }
    }
    private fun observeBottomSheetLivedata(){
    viewModel.observeBottomSheetLivedata().observe(viewLifecycleOwner, Observer {
        meal->
        Glide.with(this).load(meal.strMealThumb).into(binding.imgBtmSheet)
        binding.tvBtmSheetArea.text = meal.strArea
        binding.tvBtmSheetMealName.text = meal.strMeal
        binding.tvBtmSheetCategory.text = meal.strCategory

        mealName = meal.strMeal
        mealThump = meal.strMealThumb
    })
}
    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(mealId, param1)

                }
            }
    }
}