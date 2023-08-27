package com.example.foodcycle.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Meal::class], version = 1)
@TypeConverters(IngredientConverter::class)
abstract class MealDatabase:RoomDatabase() {

    abstract fun mealDao( ): MealDao

    companion object{
        @Volatile
        private var INSTANCE: MealDatabase? = null

        @SuppressLint("SuspiciousIndentation")
        fun getDatabase(context:  Context): MealDatabase{
             val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized( this) {
                  val instance = Room.databaseBuilder(
                      context.applicationContext,
                      MealDatabase::class.java,
                      "meal_ database"
                  ).build()
                INSTANCE = instance
                  return instance
            }
        }
    }

}