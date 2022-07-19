package com.ynk.goodnews.restapi

import com.ynk.goodnews.model.TotalNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTotalNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String?
    ): TotalNews?

    @GET("v2/top-headlines")
    suspend fun getTotalNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): TotalNews?

    @GET("v2/everything")
    suspend fun getSearchedTotalNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String
    ): TotalNews?

}