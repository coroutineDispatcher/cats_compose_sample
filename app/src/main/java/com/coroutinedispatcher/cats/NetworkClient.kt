package com.coroutinedispatcher.cats

import com.coroutinedispatcher.cats.model.Breed
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkClient {
    @GET("breeds")
    suspend fun getPosts(
        @Query("attach_breed")
        attachBreed: Int = 0
    ): List<Breed>
}
