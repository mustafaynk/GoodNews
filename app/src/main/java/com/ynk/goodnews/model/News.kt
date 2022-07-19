package com.ynk.goodnews.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class News(
    @SerializedName("source")
    @Expose
    var source: Source = Source(),

    @SerializedName("title")
    @Expose
    val newsTitle: String = String(),

    @SerializedName("description")
    @Expose
    val newsDescription: String = String(),

    @SerializedName("url")
    @Expose
    val newsUrl: String = String(),

    @SerializedName("urlToImage")
    @Expose
    val newsImage: String = String(),

    @SerializedName("publishedAt")
    @Expose
    var newsPublishedDate: Date = Date()
)