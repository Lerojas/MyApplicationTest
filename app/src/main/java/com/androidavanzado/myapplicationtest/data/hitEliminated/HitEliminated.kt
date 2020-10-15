package com.androidavanzado.myapplicationtest.data.hitEliminated

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.androidavanzado.myapplicationtest.model.EliminatedHits
import com.androidavanzado.myapplicationtest.model.Hit
import com.google.gson.Gson

class HitEliminated {

    fun saveHitEliminated(context: Context, hit: Hit){
        var eliminatedHits = EliminatedHits()
        val gson = Gson()
        val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val hitEliminatedJson : String? = sharedPreferences.getString("hitEliminated","")

        if(hitEliminatedJson!=""){
            eliminatedHits = gson.fromJson(hitEliminatedJson,EliminatedHits::class.java)

        }
        eliminatedHits.add(hit)

        val autoSaveFormsString : String = gson.toJson(eliminatedHits)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("hitEliminated", autoSaveFormsString)
        editor.apply()
    }

    fun validateHitEliminated(context: Context, hit: Hit) : Boolean {
        var hitEliminated = false
        val gson = Gson()
        val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val hitEliminatedJson : String? = sharedPreferences.getString("hitEliminated","")

        if(hitEliminatedJson!=""){
            val eliminatedHits = gson.fromJson(hitEliminatedJson,EliminatedHits::class.java)
            for(index in eliminatedHits){
                if(index.created_at==hit.created_at && index.story_title==hit.story_title){
                    hitEliminated = true
                }
            }
        }
        return hitEliminated
    }

    fun removeHitFromList(context: Context, arrayHints : ArrayList<Hit>) : ArrayList<Hit>{
        val arrayList: ArrayList<Hit> = ArrayList()
        for(hit in arrayHints){
            val isHitEliminated = validateHitEliminated(context,hit)
            if(!isHitEliminated){
                arrayList.add(hit)
            }
        }
        return arrayList
    }
}