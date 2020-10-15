package com.androidavanzado.myapplicationtest.data.api

import com.androidavanzado.myapplicationtest.model.ArticlesDataCollectionItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {
    @GET("search_by_date")
    fun listArticles(@Query("query") query : String): Call<ArticlesDataCollectionItem>
}