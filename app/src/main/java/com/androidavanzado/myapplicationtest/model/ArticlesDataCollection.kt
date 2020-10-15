package com.androidavanzado.myapplicationtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class ArticlesDataCollection: ArrayList<ArticlesDataCollectionItem>()

class EliminatedHits : ArrayList<Hit>()

data class ArticlesDataCollectionItem(
    var hits: ArrayList<Hit>
)

@Entity(tableName = Hit.TABLE_NAME)
data class Hit(
    @ColumnInfo(name = "created_at") val created_at: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "story_title", defaultValue = " ") var story_title: String,
    @ColumnInfo(name = "story_url") var story_url: String
)
{
    companion object {
        const val TABLE_NAME = "hit"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hit_id")
    var articlesId: Int = 0
}
