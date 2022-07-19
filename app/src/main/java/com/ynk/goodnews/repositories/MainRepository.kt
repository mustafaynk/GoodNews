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
        return service.getTotalNews(country, apiKey)
    }

    override suspend fun getTotalNews(
        country: String,
        category: String,
        apiKey: String
    ): TotalNews? {
        return service.getTotalNews(country, category, apiKey)
    }

    override suspend fun getSearchedTotalNews(
        keyword: String,
        apiKey: String
    ): TotalNews? {
        return service.getSearchedTotalNews(keyword, apiKey)
    }


}