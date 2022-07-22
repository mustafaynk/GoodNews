package com.ynk.goodnews.repositories

import com.ynk.goodnews.Application
import com.ynk.goodnews.model.TotalNews
import com.ynk.goodnews.restapi.NewsApi
import javax.inject.Inject

interface MainRepository {
    suspend fun getTotalNews(country: String, apiKey: String): TotalNews?

    suspend fun getTotalNews(country: String, category: String, apiKey: String): TotalNews?

    suspend fun getSearchedTotalNews(keyword: String, apiKey: String): TotalNews?
}

class MainRepositoryImpl : MainRepository {

    @Inject
    lateinit var service: NewsApi

    init {
        Application.instance.module.inject(this)
    }

    override suspend fun getTotalNews(country: String, apiKey: String): TotalNews? {
        val response = service.getTotalNews(country, apiKey)
        return if(response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getTotalNews(
        country: String,
        category: String,
        apiKey: String
    ): TotalNews? {
        val response = service.getTotalNews(country, category, apiKey)
        return if(response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getSearchedTotalNews(
        keyword: String,
        apiKey: String
    ): TotalNews? {
        val response = service.getSearchedTotalNews(keyword, apiKey)
        return if(response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }


}