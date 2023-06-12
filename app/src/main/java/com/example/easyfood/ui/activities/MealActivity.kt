package com.example.easyfood.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.data.db.MealDb
import com.example.easyfood.ui.fragments.HomeFragment
import com.example.easyfood.data.pojo.Meal
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding

class MealActivity : AppCompatActivity() {
    private lateinit var mealId : String
    private lateinit var mealName : String
    private lateinit var mealThumb : String
    private lateinit var youtubetLink :String
    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel : MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDb = MealDb.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDb)

        viewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setInfoInViews()
        onButtonSaveClick()

        viewModel.getMealDetail(mealId)
        observeMealLivedata()
        loadingCase()
        onYoutubeImgClick()

    }

    private fun onButtonSaveClick() {

        binding.btnSave.setOnClickListener {
            mealToSave?.let { it1 -> viewModel.insertMeal(it1)

            }
        }
    }

    private fun onYoutubeImgClick() {
        binding.imgYoutube.setOnClickListener { val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubetLink))
        startActivity(intent)
        }
    }
private var mealToSave:Meal? = null
    private fun observeMealLivedata() {
        viewModel.observeMealDetailLivedata().observe(this,object : Observer<Meal?>{
            override fun onChanged(t: Meal?) {
                val meal = t
                mealToSave = meal

                    onResponseCase()
                    binding.tvCategoryInfo.text = meal!!.strCategory
                    binding.tvAreaInfo.text = meal.strArea
                    binding.tvInstructions.text = meal.strInstructions
                    youtubetLink = meal.strYoutube.toString()
            }
        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
       val intent = intent
           mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
           mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
           mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.tvCategoryInfo.visibility = View.GONE
        binding.btnSave.visibility = View.GONE
        binding.tvInstructions.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.GONE
        binding.imgYoutube.visibility = View.GONE


    }

    private fun onResponseCase(){
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE


    }



}