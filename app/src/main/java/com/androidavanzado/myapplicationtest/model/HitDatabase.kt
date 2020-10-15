package com.androidavanzado.myapplicationtest.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidavanzado.myapplicationtest.data.Room.HitDao

@Database(entities = [Hit::class],version = 1)
abstract class HitDatabase : RoomDatabase(){
    abstract fun hitDao(): HitDao

    companion object{
        private const val DATABASE_NAME = "hit_database"
        @Volatile
        private var INSTANCE: HitDatabase? = null

        fun getInstance(context: Context): HitDatabase? {
            INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HitDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}