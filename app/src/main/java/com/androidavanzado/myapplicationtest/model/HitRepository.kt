package com.androidavanzado.myapplicationtest.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidavanzado.myapplicationtest.data.Room.HitDao

class HitRepository(application: Application) {
    private val hitDao: HitDao?= HitDatabase.getInstance(application)?.hitDao()

    fun insert(hit: Hit){
        if(hitDao != null) InsertAsyncTask(hitDao).execute(hit)
    }

    fun getHits(): LiveData<List<Hit>>{
        return hitDao?.getAllHits()?: MutableLiveData<List<Hit>>()
    }

    fun nukeTable() {
        if(hitDao != null) InsertAsyncTaskNukeTable(hitDao).execute()
    }

    private class InsertAsyncTask(private val hitDao: HitDao) : AsyncTask<Hit,Void, Void>(){
        override fun doInBackground(vararg params: Hit?): Void? {
            for (hit in params){
                if(hit !=null) hitDao.insert(hit)
            }
            return null
        }
    }

    private class InsertAsyncTaskNukeTable(private val hitDao: HitDao) : AsyncTask<Void,Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            hitDao.nukeTable()
            return null
        }
    }


}