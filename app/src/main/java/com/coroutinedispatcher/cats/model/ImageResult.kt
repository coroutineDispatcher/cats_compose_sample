package com.coroutinedispatcher.cats.model

import com.google.gson.annotations.SerializedName

data class ImageResult(
    @SerializedName("url")
    val url: String
)
