package com.ynk.goodnews.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("name")
    @Expose
    var sourceName: String = String(),
    @SerializedName("id")
    @Expose
    var sourceId: String = String()
)
