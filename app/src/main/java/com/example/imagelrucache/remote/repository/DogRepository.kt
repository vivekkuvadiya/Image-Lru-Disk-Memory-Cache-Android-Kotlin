package com.example.imagelrucache.remote.repository

import com.example.imagelrucache.remote.model.DogImage
import com.example.imagelrucache.utils.Resource

interface DogRepository {

    suspend fun getDogImage(): Resource<DogImage>

}