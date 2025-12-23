package com.task.newsapp.data.dataSource.remote

import com.task.newsapp.core.Constants.API_KEY
import com.task.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi{

    @GET("top-headlines")
    suspend fun getBrakingNews(
        @Query("country") countryCode: String = "eg",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

}