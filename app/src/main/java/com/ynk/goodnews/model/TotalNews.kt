package com.ynk.goodnews.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TotalNews(
    var status: String = String(),

    @SerializedName("totalResults")
    @Expose
    val totalNewsCount: Int = 0,

    @SerializedName("articles")
    @Expose
    var newsList: List<News> = ArrayList()
)
