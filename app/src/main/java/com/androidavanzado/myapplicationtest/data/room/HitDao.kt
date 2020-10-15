package com.androidavanzado.myapplicationtest.data.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androidavanzado.myapplicationtest.model.Hit

@Dao
interface HitDao {

    @Insert
    fun insert(hit: Hit)

    @Update
    fun update(hit: Hit)

    @Delete
    fun delete(hit: Hit)

    @Query("SELECT * FROM " + Hit.TABLE_NAME + " ORDER BY created_at DESC")
    fun getAllHits():LiveData<List<Hit>>

    @Query("DELETE FROM " + Hit.TABLE_NAME)
    fun nukeTable()
}