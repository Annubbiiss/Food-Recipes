package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
   private lateinit var binding: FragmentFavoritesBinding
   private lateinit var viewModel: HomeViewModel
   private lateinit var favoritesMealsAdapter: MealsAdapter
    companion object{
        const val MEAL_ID = "com.example.easyfood.ui.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.ui.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.ui.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFavorites()
        prepareFavoritesMealsAdapter()
        onFavoriteItemClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesMealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)
                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                    ,View.OnClickListener {
                        viewModel.insertMeal(deletedMeal)
                    }
                ).show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecView)
    }

    private fun prepareFavoritesMealsAdapter() {
        favoritesMealsAdapter = MealsAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.obseveFavotitesMealsLivedata().observe(viewLifecycleOwner,{
            meals->
favoritesMealsAdapter.differ.submitList(meals)
        })

    }
    private fun onFavoriteItemClick() {
        favoritesMealsAdapter.onItemClick = {
                meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)


        }
    }
}