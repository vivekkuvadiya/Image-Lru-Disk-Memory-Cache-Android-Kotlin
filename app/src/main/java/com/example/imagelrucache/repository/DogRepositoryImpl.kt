package com.example.imagelrucache.repository

import com.example.imagelrucache.remote.DogApi
import com.example.imagelrucache.remote.model.DogImage
import com.example.imagelrucache.remote.repository.DogRepository
import com.example.imagelrucache.utils.Resource
import java.io.IOException
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(private val dogApi: DogApi) : DogRepository {

    override suspend fun getDogImage(): Resource<DogImage> {
        return try {
            val response = dogApi.getDogImage()
            return if (response.isSuccessful){
                Resource.Success(response.body())
            }else{
                Resource.Error(message = response.message())
            }
        } catch (e: IOException) {
            Resource.Error(message = "No Internet connection available")
        } catch (e: Exception) {
            Resource.Error(message = e.message?:"unknown error occurred")
        }
    }

}