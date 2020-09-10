package com.coroutinedispatcher.cats

import com.coroutinedispatcher.cats.model.Breed
import com.coroutinedispatcher.cats.model.ImageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkClient {
    @GET("breeds")
    suspend fun fetchBreeds(
        @Query("attach_breed")
        attachBreed: Int = 0
    ): List<Breed>

    @GET("images/search")
    suspend fun fetchImage(
        @Query("breed_id")
        breedId: String
    ): List<ImageResult>
}
