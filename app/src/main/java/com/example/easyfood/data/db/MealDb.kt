package com.example.easyfood.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyfood.data.pojo.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealTypeConverter::class)
abstract class MealDb:RoomDatabase() {
    abstract fun mealDao():MealDao

    companion object{
        @Volatile
        var INSTANCE : MealDb? = null

        @Synchronized
        fun getInstance(context: Context):MealDb {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, MealDb::class.java, "meal.db")
                    .fallbackToDestructiveMigration().build()
            }
        return INSTANCE as MealDb}

    }
}