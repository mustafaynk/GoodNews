package com.ynk.goodnews.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("name")
    var sourceName: String = String(),
    @SerializedName("id")
    var sourceId: String = String()
)
