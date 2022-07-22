package com.ynk.goodnews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynk.goodnews.model.News
import com.ynk.goodnews.model.TotalNews
import com.ynk.goodnews.repositories.MainRepositoryImpl
import com.ynk.goodnews.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var repository = MainRepositoryImpl()
    private val newsList: MutableList<News> = mutableListOf()
    private var countryCode = ""
    private var apiKey = Util.API_KEY

    val newsLiveData: MutableLiveData<List<News>?> = MutableLiveData()

    private fun getNews(langCode: String, category: String) {
        newsList.clear()
        newsLiveData.value = null
        viewModelScope.launch {
            if (category.isNotEmpty()) {
                repository.getTotalNews(langCode, category, apiKey)?.let {
                    fillNewsList(it)
                }
            } else {
                repository.getTotalNews(langCode, apiKey)?.let {
                    fillNewsList(it)
                }
            }

        }
    }

    private fun getSearchedNews(keyword: String) {
        newsList.clear()
        newsLiveData.value = null
        viewModelScope.launch {
            repository.getSearchedTotalNews(keyword, apiKey)?.let {
                fillNewsList(it)
            }
        }
    }

    private fun fillNewsList(totalNews: TotalNews?) {
        totalNews?.let {
            newsList.addAll(it.newsList)
            newsLiveData.postValue(newsList)
        }
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
        getNews(countryCode, "")
    }

    fun newsCategoryClick(category: Any) {
        getNews(countryCode, category.toString())
    }

    fun searchNews(keyword: String) {
        getSearchedNews(keyword)
    }

}