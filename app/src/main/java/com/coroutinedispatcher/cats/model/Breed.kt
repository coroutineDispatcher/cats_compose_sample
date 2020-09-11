package com.coroutinedispatcher.cats.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Breed(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("wikipedia_url")
    val wikipediaUrl: String,
    @Expose(serialize = false)
    var imageUrl: String = ""
)
