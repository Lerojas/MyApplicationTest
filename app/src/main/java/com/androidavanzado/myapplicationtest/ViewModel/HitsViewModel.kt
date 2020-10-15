package com.androidavanzado.myapplicationtest.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.androidavanzado.myapplicationtest.model.Hit
import com.androidavanzado.myapplicationtest.model.HitRepository

class HitsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HitRepository(application)
    val hits = repository.getHits()

    fun saveHit(hit: Hit){
        repository.insert(hit)
    }

    fun nakeTable(){
        repository.nukeTable()
    }

    fun saveDataRoom(arrayList: ArrayList<Hit>){
        nakeTable()
        for (hit in arrayList){
            if(hit.story_title==null) {
                hit.story_title = " "
            }

            if(hit.story_url==null) {
                hit.story_url = " "
            }

            saveHit(hit)
        }
    }
}