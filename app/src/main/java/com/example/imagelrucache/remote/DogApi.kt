package com.example.imagelrucache.remote

import com.example.imagelrucache.remote.model.DogImage
import retrofit2.Response
import retrofit2.http.GET

interface DogApi {

    @GET("image/random")
    suspend fun getDogImage():Response<DogImage>

}